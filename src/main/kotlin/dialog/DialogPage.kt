package me.znotchill.blossom.dialog

class DialogPage(val index: Int) {
    internal val actions = mutableListOf<DialogButton>()
    internal val tabs = mutableListOf<DialogTab>()

    fun actions(block: DialogActionsScope.() -> Unit) {
        val scope = DialogActionsScope().apply(block)
        actions.addAll(scope.buttons)
    }

    fun tabs(block: DialogTabsScope.() -> Unit) {
        val scope = DialogTabsScope().apply(block)
        tabs.addAll(scope.tabs)
    }
}