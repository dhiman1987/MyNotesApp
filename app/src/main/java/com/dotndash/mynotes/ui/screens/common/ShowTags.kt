package com.dotndash.mynotes.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dotndash.mynotes.data.Tag
import com.dotndash.mynotes.ui.theme.DotnDashTheme

@Composable
fun ShowTags(tags: List<Tag>?,
             backgroundColor: Color,
             deleteEnabled: Boolean,
             onDelete: (Tag) -> Unit){
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        if (tags != null) {
            tags.forEach { tag ->
                Row(
                    modifier = Modifier
                        .clickable { onDelete(tag) }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onSecondary,
                            shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 4.dp, vertical = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tag.name,
                        modifier = Modifier
                            .padding(4.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                    if(deleteEnabled){
                        Icon(
                            Icons.Filled.Close, contentDescription = "remove tag",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                }
            }
        } else {
            Text(text="no tags",
                modifier = Modifier.padding(
                    horizontal = 12.dp, vertical = 4.dp
                ),
                color = Color.Gray,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic)
        }

    }
}

@Composable
@Preview
fun ShowTagsPreview(){
    val tags = ArrayList<Tag>(3)
    tags.add(Tag(1L,"Journal"))
    tags.add(Tag(2L,"Office"))
    tags.add(Tag(3L,"Confidential"))

    DotnDashTheme(darkTheme = true) {
        ShowTags(tags, backgroundColor = MaterialTheme.colorScheme.primary,true, onDelete = {})
    }
}

