package Model

import java.lang.reflect.Constructor

class GroupMakingModel {
    var groupName : String? = null
    var groupCount : Int=0

    constructor()

    constructor(groupName: String?, groupCount: Int){
        this.groupName = groupName
        this.groupCount = groupCount
    }

}