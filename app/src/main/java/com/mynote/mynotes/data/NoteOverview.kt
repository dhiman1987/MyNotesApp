package com.mynote.mynotes.data

data class NoteOverview(val id: String,
                        val title: String,
                        val strongEncryption: Boolean,
                        var tags: List<Tag>?,
                        val updatedOn: String)
