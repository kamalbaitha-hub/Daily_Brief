package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InitialData

const val CATEGORY_RECOMMENDATIONS = "Recommendations"
const val CATEGORY_NEWSPAPERS = "Newspaper Corner"
const val CATEGORY_MAGAZINES = "Magazine Corner"

@Composable
fun CategoryChipRow(
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All Briefs" chip
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = {
                Text(
                    text = "All Categories",
                    fontWeight = if (selectedCategory == null) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AllInclusive,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("category_chip_all")
        )

        // "Newspaper Corner" chip
        val isNewsSelected = selectedCategory == CATEGORY_NEWSPAPERS
        FilterChip(
            selected = isNewsSelected,
            onClick = {
                if (isNewsSelected) onCategorySelected(null) else onCategorySelected(CATEGORY_NEWSPAPERS)
            },
            label = {
                Text(
                    text = "Newspaper Corner",
                    fontWeight = if (isNewsSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Newspaper,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("category_chip_newspapers")
        )

        // "Magazine Corner" chip
        val isMagSelected = selectedCategory == CATEGORY_MAGAZINES
        FilterChip(
            selected = isMagSelected,
            onClick = {
                if (isMagSelected) onCategorySelected(null) else onCategorySelected(CATEGORY_MAGAZINES)
            },
            label = {
                Text(
                    text = "Magazine Corner",
                    fontWeight = if (isMagSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("category_chip_magazines")
        )

        // "Recommendations" chip (Stock Picks & Mutual Funds)
        val isRecSelected = selectedCategory == CATEGORY_RECOMMENDATIONS
        FilterChip(
            selected = isRecSelected,
            onClick = {
                if (isRecSelected) onCategorySelected(null) else onCategorySelected(CATEGORY_RECOMMENDATIONS)
            },
            label = {
                Text(
                    text = "Recommendations",
                    fontWeight = if (isRecSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AutoGraph,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("category_chip_recommendations")
        )

        // Specific category chips
        InitialData.ALL_CATEGORIES.forEachIndexed { index, cat ->
            val isSelected = selectedCategory?.equals(cat, ignoreCase = true) == true
            val (_, catIcon) = getCategoryStyle(cat)

            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        onCategorySelected(null)
                    } else {
                        onCategorySelected(cat)
                    }
                },
                label = {
                    Text(
                        text = cat,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = catIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.testTag("category_chip_$index")
            )
        }
    }
}
