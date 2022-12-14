package com.jds_code.theeverythingbagel

import android.util.Log
import androidx.lifecycle.*
import com.jds_code.theeverythingbagel.database.notes.Notes
import com.jds_code.theeverythingbagel.database.notes.NotesDao
import kotlinx.coroutines.launch

// 12/10/2022 @ 13:15: '(private val notesDao: NotesDao)' was causing
//  app to crash but it seems the issue was with 'sharedViewModel' vs.
//  'viewModel'
class TEBViewModel(private val notesDao: NotesDao) : ViewModel() {

    // These had been in NewNoteFragment.kt but should now be here in TEBViewModel.
    //  Also, we need these to have backing backing properties like this so the
    //  _variable is editable and accessible only in TEBViewModel.
    /*private var _noteTitle = MutableLiveData<String>("")
    val noteTitle: LiveData<String> = _noteTitle

    private var _noteBody = MutableLiveData<String>("")
    val noteBody: LiveData<String> = _noteBody*/

/*    init {
        Log.d("TEBViewModel", "TEBViewModel has been created.")
    }*/

    /**
     * Will be used to show items from Room DB in Notes List Fragment
     */
    val allNotes: LiveData<List<Notes>> = notesDao.getItems().asLiveData()

    /**
     * Adds new note to the database
     */
    fun addNewNote(noteTitle: String, noteBody: String) {
        val newNote = getNewNoteEntry(noteTitle, noteBody)
        insertNote(newNote)
    }

    private fun insertNote(notes: Notes) {
        viewModelScope.launch {
            notesDao.insert(notes)
        }
    }

    /**
     * Checks that the entries by the user are not blank.
     */
    fun isEntryValid(noteTitle: String, noteBody: String): Boolean {
        if (noteTitle.isBlank() || noteBody.isBlank()) {
            return false
        }
        return true
    }

    /**
     * This returns an instance of the [Notes] entity class with the note info entered.
     * This will be used to add a new note to the Notes database.
     */
    private fun getNewNoteEntry(noteTitle: String, noteBody: String): Notes {
        return Notes(
            noteTitle = noteTitle,
            noteBody = noteBody
        )
    }


    // I'm not 100% certain, but I think this is the best way to save the note
    //  title and body the user inputs.
    /*fun setNoteTitle(newNoteTitle: String) {
        _noteTitle.value = newNoteTitle
    }

    fun setNoteBody(newNoteBody: String) {
        _noteBody.value = newNoteBody
    }*/

    // I don't know that we need this.
    /*override fun onCleared() {
        super.onCleared()
        Log.d("TEBViewModel", "TEBViewModel has been destroyed.")
    }*/
}


class NotesViewModelFactory(private val notesDao: NotesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TEBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TEBViewModel(notesDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
