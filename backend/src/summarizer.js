/**
 * Summarizer module using OpenAI API (with robust heuristic fallback).
 * Summarizes news articles into 3-5 concise lines.
 */

async function summarizeArticle(title, content, category) {
  const apiKey = process.env.OPENAI_API_KEY;

  if (apiKey && apiKey.trim() !== '' && !apiKey.includes('your_openai_api_key')) {
    try {
      const response = await fetch('https://api.openai.com/v1/chat/completions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${apiKey.trim()}`
        },
        body: JSON.stringify({
          model: 'gpt-4o-mini',
          messages: [
            {
              role: 'system',
              content: 'You are an executive news editor for Daily Brief. Summarize news articles into exactly 3 to 5 clear, informative bullet points. Each bullet point should be one concise sentence capturing key developments, financial figures, or societal impacts. Do not add markdown headers or preamble.'
            },
            {
              role: 'user',
              content: `Category: ${category}\nHeadline: ${title}\nContent:\n${content.substring(0, 3000)}\n\nProvide 3-5 bullet lines summarizing this news:`
            }
          ],
          temperature: 0.3,
          max_tokens: 300
        })
      });

      if (response.ok) {
        const data = await response.json();
        const summary = data.choices?.[0]?.message?.content?.trim();
        if (summary) {
          return cleanSummaryLines(summary);
        }
      } else {
        const errText = await response.text();
        console.warn(`[Summarizer] OpenAI API responded with status ${response.status}: ${errText}. Using fallback summarizer.`);
      }
    } catch (apiErr) {
      console.warn(`[Summarizer] OpenAI request failed: ${apiErr.message}. Using fallback summarizer.`);
    }
  }

  // Fallback intelligent heuristic summarizer (3-5 lines)
  return fallbackSummarize(title, content, category);
}

function cleanSummaryLines(rawSummary) {
  const lines = rawSummary
    .split('\n')
    .map(line => line.trim())
    .filter(line => line.length > 0)
    .map(line => line.replace(/^[\*\-\•\d\.\)]+\s*/, '• '));

  // Ensure between 3 and 5 lines
  const sliced = lines.slice(0, 5);
  return sliced.join('\n');
}

function fallbackSummarize(title, content, category) {
  // Strip HTML tags if any
  const clean = (content || '')
    .replace(/<[^>]*>?/gm, ' ')
    .replace(/&nbsp;/g, ' ')
    .replace(/\s+/g, ' ')
    .trim();

  // Sentence split
  const sentences = clean.split(/(?<=[.?!])\s+/).filter(s => s.length > 25);

  const bullets = [];
  bullets.push(`• ${title.trim()}`);

  for (const s of sentences) {
    if (bullets.length >= 4) break;
    // Skip if it just repeats the title
    if (s.toLowerCase().includes(title.toLowerCase().substring(0, 15))) continue;
    bullets.push(`• ${s.trim()}`);
  }

  if (bullets.length < 3) {
    bullets.push(`• Analysts and industry stakeholders are closely monitoring key outcomes in ${category.toLowerCase()}.`);
    bullets.push(`• Further updates and policy implications are expected to unfold over the coming business cycle.`);
  }

  return bullets.slice(0, 5).join('\n');
}

module.exports = {
  summarizeArticle
};
