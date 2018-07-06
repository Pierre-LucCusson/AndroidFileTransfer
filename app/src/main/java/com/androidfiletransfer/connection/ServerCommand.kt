package com.androidfiletransfer.connection

class ServerCommand {

    companion object {
        const val GET_CONTACTS_LIST = "/get_contacts_list"
        const val GET_CONTACT = "/get_contact"
        const val GET_FILES_LIST = "/get_files_list"
        const val GET_FILE = "/get_file"
        const val GET_LOCATION = "/get_location"
        const val DOES_NOT_EXIST = "/command_does_not_exist"
    }

    fun get(uri : String) : String {

        return when {
            uri.contains(GET_CONTACTS_LIST) -> GET_CONTACTS_LIST
            uri.contains(GET_CONTACT) -> GET_CONTACT
            uri.contains(GET_FILES_LIST) -> GET_FILES_LIST
            uri.contains(GET_FILE) -> GET_FILE
            uri.contains(GET_LOCATION) -> GET_LOCATION
            else -> DOES_NOT_EXIST
        }

    }

}