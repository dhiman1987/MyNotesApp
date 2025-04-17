package com.mynote.mynotes.data

import com.mynote.mynotes.db.TagEntity

data class Tag(val id: Long, val name: String)

fun Tag.toTagEntity(): TagEntity = TagEntity(id=id,name=name)


