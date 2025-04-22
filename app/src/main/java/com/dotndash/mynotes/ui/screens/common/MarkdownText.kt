package com.dotndash.mynotes.ui.screens.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.dotndash.mynotes.ui.theme.DotnDashTheme

@Composable
fun MarkdownText(
    text: String,
    headingSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    subHeadingSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    bodySize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    modifier: Modifier = Modifier
) {
    val styledText = buildAnnotatedString {
        val lines = text.split("\n")
        for (line in lines) {
            when {
                line.startsWith("# ") -> {
                    withStyle(style = SpanStyle(
                        fontSize = headingSize,
                        fontWeight = FontWeight.Bold)) {
                        append(line.removePrefix("# "))
                        append("\n")
                    }
                }
                line.startsWith("## ") -> {
                    withStyle(style = SpanStyle(
                        fontSize = subHeadingSize,
                        fontWeight = FontWeight.SemiBold)) {
                        append(line.removePrefix("## "))
                        append("\n")
                    }
                }
                line.startsWith("- ") -> {
                    withStyle(style = SpanStyle(
                        fontSize = bodySize)) {
                        append("• ")
                        append(line.removePrefix("- "))
                    }
                }
                line.startsWith("  - ") -> {
                    withStyle(style = SpanStyle(
                        fontSize = bodySize)) {
                        append("   • ")
                        append(line.removePrefix("  - "))
                    }
                }
                else -> {
                    var currentIndex = 0
                    while (currentIndex < line.length) {
                        when {
                            line.startsWith("**", currentIndex) -> {
                                val endIndex = line.indexOf("**", currentIndex + 2)
                                if (endIndex != -1) {
                                    withStyle(style = SpanStyle(
                                        fontWeight = FontWeight.Bold)) {
                                        append(line.substring(currentIndex + 2, endIndex))
                                    }
                                    currentIndex = endIndex + 2
                                } else {
                                    append(line.substring(currentIndex))
                                    currentIndex = line.length
                                }
                            }
                            line.startsWith("*", currentIndex) -> {
                                val endIndex = line.indexOf("*", currentIndex + 1)
                                if (endIndex != -1) {
                                    withStyle(style = SpanStyle(
                                        fontStyle = FontStyle.Italic)) {
                                        append(line.substring(currentIndex + 1, endIndex))
                                    }
                                    currentIndex = endIndex + 1
                                } else {
                                    append(line.substring(currentIndex))
                                    currentIndex = line.length
                                }
                            }
                            line.startsWith("__", currentIndex) -> {
                                val endIndex = line.indexOf("__", currentIndex + 2)
                                if (endIndex != -1) {
                                    withStyle(style = SpanStyle(
                                        textDecoration = TextDecoration.Underline)) {
                                        append(line.substring(currentIndex + 2, endIndex))
                                    }
                                    currentIndex = endIndex + 2
                                } else {
                                    append(line.substring(currentIndex))
                                    currentIndex = line.length
                                }
                            }
                            else -> {
                                append(line[currentIndex])
                                currentIndex++
                            }
                        }
                    }
                }
            }
            append("\n")
        }
    }
    Text(text = styledText, modifier = modifier)
}

@Composable
@Preview
fun MarkdownTextPreview() {
    val markdownText = """
        # Heading 1
        ## Subheading 1
        - Bullet 1
        - Bullet 2
          - Sub-bullet 1
        **Bold Text**
        *Italic Text*
        __Underline Text__
    """.trimIndent()
    DotnDashTheme(darkTheme = false) {
        MarkdownText(text = markdownText)
    }

}
