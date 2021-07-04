package com.spartancookie.formatter

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


private const val validXMLUnformatted = "<?xml version=\"1.0\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title></book><book id=\"bk111\"><author>O'Brien, Tim</author><title>MSXML3: A Comprehensive Guide</title></book><book id=\"bk112\"><author>Galos, Mike</author><title>Visual Studio 7: A Comprehensive Guide</title></book></catalog>"
private const val validXMLFormatted = "<catalog>\r\n" + "    <book id=\"bk101\">\r\n" + "        <author>Gambardella, Matthew</author>\r\n" + "        <title>XML Developer's Guide</title>\r\n" + "    </book>\r\n" + "    <book id=\"bk111\">\r\n" + "        <author>O'Brien, Tim</author>\r\n" + "        <title>MSXML3: A Comprehensive Guide</title>\r\n" + "    </book>\r\n" + "    <book id=\"bk112\">\r\n" + "        <author>Galos, Mike</author>\r\n" + "        <title>Visual Studio 7: A Comprehensive Guide</title>\r\n" + "    </book>\r\n" + "</catalog>\r\n"

private const val invalidXML = "<?xml version=\"1.0\"?><catalog><book id=\"bk101\"><author>Gambardella,"


private const val validJSONUnformatted = "{\"glossary\": {\"title\": \"example glossary\",\"GlossDiv\": {\"title\": \"S\",\"GlossList\": {\"GlossEntry\": {\"ID\": \"SGML\",\"SortAs\": \"SGML\",\"GlossTerm\": \"Standard Generalized Markup Language\",\"Acronym\": \"SGML\",\"Abbrev\": \"ISO 8879:1986\",\"GlossDef\": {\"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\": [\"GML\", \"XML\"]},\"GlossSee\": \"markup\"}}}}}"
private const val validJSONFormatted = "{\r\n  \"glossary\" : {\r\n    \"title\" : \"example glossary\",\r\n    \"GlossDiv\" : {\r\n      \"title\" : \"S\",\r\n      \"GlossList\" : {\r\n        \"GlossEntry\" : {\r\n          \"ID\" : \"SGML\",\r\n          \"SortAs\" : \"SGML\",\r\n          \"GlossTerm\" : \"Standard Generalized Markup Language\",\r\n          \"Acronym\" : \"SGML\",\r\n          \"Abbrev\" : \"ISO 8879:1986\",\r\n          \"GlossDef\" : {\r\n            \"para\" : \"A meta-markup language, used to create markup languages such as DocBook.\",\r\n            \"GlossSeeAlso\" : [ \"GML\", \"XML\" ]\r\n          },\r\n          \"GlossSee\" : \"markup\"\r\n        }\r\n      }\r\n    }\r\n  }\r\n}"

private const val invalidJSON = "\"SGML\": \"random\",\"SortAs\": \"SGML\",\"GlossTerm\": \"Standard Generalized Markup Language\",\"Acronym\": \"SGMLeate markup languages such as DocBook.\",\"GlossSeeAlso\": [\"GML\", \"XML\"]},\"GlossSee\": \"markup\"}}}}}"

@RunWith(JUnit4::class)
class FormatterTest {

    @Test
    fun formatValidXML() {
        val result = Formatter.format(DataType.XML, validXMLUnformatted)
        // Assure content is formatted correctly
        assertEquals(validXMLFormatted, result)
    }

    @Test
    fun formatInvalidXML() {
        val result = try {
            Formatter.format(DataType.XML, invalidXML)
        } catch (e: Exception) {
            invalidXML
        }
        // Assure it returned the inputted content
        assertEquals(invalidXML, result)
    }

    @Test
    fun formatValidJSON() {
        val result = Formatter.format(DataType.JSON, validJSONUnformatted)
        // Assure content is formatted correctly
        assertEquals(validJSONFormatted, result)
    }

    @Test
    fun formatInvalidJSON() {
        val result = try {
            Formatter.format(DataType.JSON, invalidJSON)
        } catch (e: Exception) {
            invalidJSON
        }
        // Assure it returned the inputted content
        assertEquals(invalidJSON, result)
    }
}