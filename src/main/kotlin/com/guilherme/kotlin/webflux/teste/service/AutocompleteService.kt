package com.guilherme.kotlin.webflux.teste.service

import com.guilherme.kotlin.webflux.teste.model.PartnerAutocomplete
import com.guilherme.kotlin.webflux.teste.repository.AutocompleteRepository
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.core.LowerCaseFilter
import org.apache.lucene.analysis.core.WhitespaceTokenizer
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter
import org.apache.lucene.analysis.ngram.NGramTokenFilter
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.RAMDirectory
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AutocompleteService(private val autocompleteRepository: AutocompleteRepository) {

    private val directory = RAMDirectory()
    private lateinit var date: LocalDateTime
    private val logger = LoggerFactory.getLogger(AutocompleteService::class.java)
    private val expirationInHour = 1

    init {
        loadLucene()
    }

    fun search(value: String, limit: Int): List<PartnerAutocomplete> {

        if(ChronoUnit.MINUTES.between(date, LocalDateTime.now()) >= expirationInHour) {
            logger.info("Cache's gone to hell")
            loadLucene()
        }

        val s = searcher(directory)
        val parser = MultiFieldQueryParser(arrayOf("id", "name"), analyzer())
        val q = parser.parse(value)
        return s.search(q, limit).scoreDocs
                .map { s.doc(it.doc) }
                .map { PartnerAutocomplete(it.get("id"), it.get("name"), it.get("type")) }

    }

    private fun writer(directory: RAMDirectory, analyzer: Analyzer): IndexWriter {
        val config = IndexWriterConfig(analyzer)
        return IndexWriter(directory, config)
    }

    private fun searcher(directory: RAMDirectory): IndexSearcher {
        val reader = DirectoryReader.open(directory)
        return IndexSearcher(reader)
    }

    private fun partnerDocuments(): List<Document> {
        return autocompleteRepository.findAll().map {
            val d = Document()
            d.add(TextField("id", it.id, Field.Store.YES))
            d.add(TextField("name", it.name, Field.Store.YES))
            d.add(TextField("type", it.type, Field.Store.YES))
            d
        }
    }

    private fun analyzer(): Analyzer {
        val source = WhitespaceTokenizer()
        val lowerCaseFilter = LowerCaseFilter(source)
        val asciiFoldingFilter = ASCIIFoldingFilter(lowerCaseFilter)
        return NgramAnalyzer(Analyzer.TokenStreamComponents(source, NGramTokenFilter(asciiFoldingFilter, 3, 30)))
    }

    private fun loadLucene() {
        logger.info("Initiating lucene index refresh")
        val writer = writer(directory, analyzer())
        writer.deleteAll()
        val docs = partnerDocuments()
        writer.addDocuments(docs)
        writer.commit()
        writer.close()
        this.date = LocalDateTime.now()
        logger.info("Lucene index refresh complete")
    }

}

class NgramAnalyzer(private val components: Analyzer.TokenStreamComponents) : Analyzer() {

    override fun createComponents(fieldName: String): Analyzer.TokenStreamComponents {
        return components
    }

}