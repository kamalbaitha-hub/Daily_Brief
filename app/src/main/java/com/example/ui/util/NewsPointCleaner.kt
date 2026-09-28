package com.example.ui.util

object NewsPointCleaner {

    private val PREFIX_REGEX = Regex(
        "^(Key\\s*context|Details?|Context|Live\\s*coverage|Executive\\s*summary|Summary|Update|Analysis|Overview|Background|Highlights?)\\s*[:\\-]\\s*",
        RegexOption.IGNORE_CASE
    )

    private val BOILERPLATE_PATTERNS = listOf(
        "verified real-time reporting",
        "verified real time reporting",
        "fast executive summary",
        "compiled directly from live news feeds",
        "real-time news wire",
        "live coverage:"
    )

    /**
     * Extracts pure, clean summary main points from raw summary text and original content.
     * Strips point titles like "Key context:", "Details:", etc.
     * Removes useless boilerplate lines like "Verified real time reporting", "fast executive summary".
     * Excludes lines that repeat the headline.
     */
    fun extractCleanPoints(
        summary: String,
        headline: String,
        originalContent: String = "",
        maxPoints: Int = 4
    ): List<String> {
        val normHeadline = headline.lowercase().replace(Regex("[^a-z0-9]"), " ").trim()
        val cleanedPoints = mutableListOf<String>()

        val rawLines = summary.split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        for (line in rawLines) {
            val clean = cleanSinglePoint(line, normHeadline)
            if (clean != null && !cleanedPoints.any { it.equals(clean, ignoreCase = true) }) {
                cleanedPoints.add(clean)
                if (cleanedPoints.size >= maxPoints) break
            }
        }

        // If summary didn't yield enough clean points, extract from originalContent sentences
        if (cleanedPoints.size < 2 && originalContent.isNotBlank()) {
            val strippedContent = originalContent
                .replace(Regex("<[^>]*>"), " ")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace(Regex("\\s+"), " ")
                .trim()

            val sentences = strippedContent.split(Regex("(?<=[.!?])\\s+"))
                .map { it.trim() }
                .filter { it.length > 25 }

            for (s in sentences) {
                val clean = cleanSinglePoint(s, normHeadline)
                if (clean != null && !cleanedPoints.any { it.contains(clean.take(30), ignoreCase = true) }) {
                    cleanedPoints.add(clean)
                    if (cleanedPoints.size >= maxPoints) break
                }
            }
        }

        return cleanedPoints
    }

    fun cleanSinglePoint(rawLine: String, normHeadline: String): String? {
        var text = rawLine.trim()

        // Strip leading bullets/numbering: "• ", "- ", "* ", "1. "
        text = text.replace(Regex("^[•\\-*\\d.]+\\s*"), "").trim()

        // Strip HTML tags if any slipped in
        text = text.replace(Regex("<[^>]*>"), " ").replace(Regex("\\s+"), " ").trim()

        val lower = text.lowercase()

        // Discard boilerplate filler lines entirely
        for (pattern in BOILERPLATE_PATTERNS) {
            if (lower.contains(pattern)) {
                return null
            }
        }

        // Strip point titles like "Key context:", "Details:", "Context:", etc.
        text = text.replace(PREFIX_REGEX, "").trim()

        // Discard if empty or too short
        if (text.length < 12) return null

        // Exclude point if it just repeats the headline
        val normClean = text.lowercase().replace(Regex("[^a-z0-9]"), " ").trim()
        if (normHeadline.length > 15) {
            if (normClean.contains(normHeadline) || normHeadline.contains(normClean)) {
                return null
            }
        }

        // Capitalize first letter
        val capitalized = text.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        return capitalized
    }
}
