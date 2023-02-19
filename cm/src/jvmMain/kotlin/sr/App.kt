package sr

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.ui.input.key.Key
import todo.onKeyUp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow

@Composable
@Preview
fun App() {
    MaterialTheme {
        RootContent(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
) {
    val rootStore = remember { RootStore() }
    val state = rootStore.state

    MainContent(
        modifier = modifier,
        inputText = state.searchText,
        onSearchTextChanged = rootStore::onSearchTextChanged,
        onSearchTextClicked = rootStore::onSearchTextClicked,
        searchItems = state.searchItems,
        onSearchItemsClicked = rootStore::onSearchItemsClicked,
    )
}

@Composable
internal fun MainContent(
    modifier: Modifier = Modifier,
    inputText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearchTextClicked: () -> Unit,
    searchItems: List<SearchItem>,
    onSearchItemsClicked: (searchItem: SearchItem) -> Unit,
) {
    Column(modifier) {
        SearchInput(
            text = inputText,
            onTextChanged = onSearchTextChanged,
            onTextClicked = onSearchTextClicked,
        )
        Box(Modifier.weight(1F)) {
            SearchItemListContent(
                searchItems = searchItems,
                onSearchItemsClicked = onSearchItemsClicked,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchInput(
    text: String,
    onTextChanged: (String) -> Unit,
    onTextClicked: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            value = text,
            modifier = Modifier
                .weight(weight = 1F)
                .onKeyUp(key = Key.Enter, action = onTextClicked),
            onValueChange = onTextChanged,
            label = { Text(text = "") }
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun SearchItemListContent(
    searchItems: List<SearchItem>,
    onSearchItemsClicked: (searchItem: SearchItem) -> Unit,
) {
    Box {
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
            items(searchItems) { item ->
                Row(modifier = Modifier.clickable(onClick = { onSearchItemsClicked(item) })) {
                    Text(
                        text = AnnotatedString(item.text),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = listState)
        )
    }
}

data class SearchItem(val text: String = "")

data class RootState(
    val searchText: String = "",
    val searchItems: List<SearchItem> = listOf(),
    val storeSearchItems: List<SearchItem> = listOf(),
)

class RootStore {
    fun onSearchTextChanged(text: String) {
        val searchedSearchItems = mutableListOf<SearchItem>()
        if (text.length > 1) {
            searchedSearchItems.addAll(state.storeSearchItems.filter { it.text.startsWith(text) })
        }
        setState { copy(searchText = text, searchItems = searchedSearchItems) }
    }

    fun onSearchTextClicked() {
        setState { copy(searchText = "") }
    }

    fun onSearchItemsClicked(searchItem: SearchItem) {
        setState { copy(searchText = searchItem.text) }
    }

    var state: RootState by mutableStateOf(initialState())
        private set

    private fun initialState(): RootState = RootState(
        searchText = "",
        searchItems = listOf(),
        storeSearchItems = listOf(
            SearchItem("function"),
            SearchItem("class"),
            SearchItem("abstract"),
        ),
    )

    private inline fun setState(update: RootState.() -> RootState) {
        state = state.update()
    }
}
