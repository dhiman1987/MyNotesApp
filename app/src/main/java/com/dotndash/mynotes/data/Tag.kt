package com.dotndash.mynotes.data

import com.dotndash.mynotes.db.TagEntity

data class Tag(val id: Long, val name: String)

fun Tag.toTagEntity(): TagEntity = TagEntity(id=id,name=name)


