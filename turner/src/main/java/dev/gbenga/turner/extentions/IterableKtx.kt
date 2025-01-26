package dev.gbenga.turner.extentions


fun <T> Iterable<T>.forEachIndexed(start: Int, action: (Int, T) -> Unit){
    this.forEachIndexed{i, data ->
        val index = start + i
        if (index <= this.count()){
            action(index, data)
        }else{
            return
        }
    }
}