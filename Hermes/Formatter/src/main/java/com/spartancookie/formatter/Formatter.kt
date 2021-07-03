package com.spartancookie.formatter

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

private const val DEFAULT_INDENT = 4

private const val XML_INDENT_PROPERTY_NAME = "{http://xml.apache.org/xslt}indent-amount"

class Formatter private constructor() {

    companion object {

        private const val TAG = "Formatter"

        @JvmStatic
        fun format(dataType: DataType, content: String): String {
            return try {
                when (dataType) {
                    DataType.XML -> Formatter().prettifyXML(content, DEFAULT_INDENT)
                    DataType.JSON -> Formatter().prettifyJSON(content)
                }
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Could not prettify given string, returning input string. exception: ${
                        RuntimeException(e)
                    }"
                )
                content
            }
        }

    }

    /**
     * Prettify XML content
     * @param xmlStr String in XML format
     * @param indent Indentation spaces
     */
    fun prettifyXML(xmlStr: String, indent: Int): String {
        // Turn xml string into a document
        val document = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(InputSource(ByteArrayInputStream(xmlStr.toByteArray(charset(Charsets.UTF_8.name())))))

        // Remove whitespaces outside tags
        document.normalize()
        val xPath: XPath = XPathFactory.newInstance().newXPath()
        val nodeList: NodeList = xPath.evaluate(
            "//text()[normalize-space()='']",
            document,
            XPathConstants.NODESET
        ) as NodeList
        for (i in 0 until nodeList.length) {
            val node: Node = nodeList.item(i)
            node.parentNode.removeChild(node)
        }

        // Setup pretty print options
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer().apply {
            setOutputProperty(XML_INDENT_PROPERTY_NAME, indent.toString())
            setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            setOutputProperty(OutputKeys.INDENT, "yes")
        }

        // Return pretty print xml string
        val stringWriter = StringWriter()
        transformer.transform(DOMSource(document), StreamResult(stringWriter))
        return stringWriter.toString()
    }

    /**
     * Prettify JSON content
     * @param jsonStr String in JSON format
     */
    fun prettifyJSON(jsonStr: String): String {
        val objectMapper = ObjectMapper().apply {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
        val tree = objectMapper.readTree(jsonStr)
        return objectMapper.writeValueAsString(tree)
    }

}
