package com.coding.flickertask.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coding.flickertask.R
import com.coding.flickertask.databinding.FragmentPhotoListBinding
import com.coding.flickertask.model.PhotoFragmentEvent
import com.coding.flickertask.model.PhotoFragmentViewState
import com.coding.flickertask.model.PhotoResponse
import com.coding.flickertask.presentation.extensions.hideKeyboard
import com.coding.flickertask.presentation.ui.list.adapter.PhotosLoadStateAdapter
import com.coding.flickertask.presentation.ui.list.adapter.PhotosItemAdapter
import com.coding.flickertask.presentation.ui.list.callback.OnItemClickListener
import com.coding.flickertask.presentation.ui.list.style.VerticalSpaceOnItems
import com.coding.flickertask.util.ConnectivityStatus
import com.coding.flickertask.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
class PhotoListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    companion object {
        private val TAG = PhotoListFragment::class.qualifiedName
        fun newInstance(): PhotoListFragment {
            return PhotoListFragment()
        }
    }

    private var isInternetActive = false
    private lateinit var binding: FragmentPhotoListBinding
    private val photosAdapter: PhotosItemAdapter =
        PhotosItemAdapter(listener = this)

    // Lazy Inject ViewModel
    private val viewModel by sharedViewModel<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        observeViewState()
        // observeUserHistory()
        setupInternetObserver()
    }

    private fun setupInternetObserver() {
        val connectivity = context?.let { ConnectivityStatus(it) }
        connectivity?.observe(viewLifecycleOwner, Observer { isConnected ->
            isInternetActive = if (isConnected) {
                isConnected
            } else {
                false
            }
        })
    }

    private fun observeUserHistory() {
        viewModel.userHistoryState.observe(viewLifecycleOwner) {
            val adapter = context?.let { context ->
                ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    it
                )
            }
            binding.searchInputText.setAdapter(adapter)
            binding.searchInputText.setOnItemClickListener { _, _, _, _ ->
                fetchPhotoList()
            }
        }
    }

    private fun setupBinding() {
        binding.pullToRefresh.setOnRefreshListener(this)
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceOnItems(resources.getDimensionPixelSize(R.dimen.list_item_decoration)))
            initAdapter()
        }
        binding.retryButton.setOnClickListener {
            binding.errorSection.visibility = View.GONE
            fetchPhotoList()
        }

        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchPhotoList()
            }
            true
        }
        viewModel.getLocallySavedData(PhotoFragmentEvent.loadLocallySaved)
        binding.searchButton.setOnClickListener { fetchPhotoList() }
    }

    private fun fetchPhotoList() {
        /*
           Hide the keyboard while fetching data from the server
         */
        hideKeyboard()

        lifecycleScope.launch {
            /*
            check null while input text is not there
             */
            if (isInternetActive) {
                binding.searchInputText.text.let {
                    if (binding.searchInputText.text.toString().trim() != "") {
                        binding.pullToRefresh.isRefreshing = true
                        viewModel.onSuspendedEvent(
                            PhotoFragmentEvent.ScreenLoad,
                            binding.searchInputText.text.toString().trim()
                        )
                    }
                    /*
                        hide pull to refresh indicator while applying pull to refresh on empty or null search text
                     */
                    else binding.pullToRefresh.isRefreshing = false
                }
            } else {

                render(
                    PhotoFragmentViewState(
                        null, mutableListOf(), R.string.no_internet_message, null,
                        null, View.VISIBLE
                    )
                )
            }

        }
    }


    private fun initAdapter() {
        binding.list.adapter = photosAdapter.withLoadStateHeaderAndFooter(
            header = PhotosLoadStateAdapter { photosAdapter.retry() },
            footer = PhotosLoadStateAdapter { photosAdapter.retry() }
        )
        setScrollToTopWHenRefreshedFromNetwork()
    }

    private fun setScrollToTopWHenRefreshedFromNetwork() {
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            photosAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    private fun observeViewState() {
        viewModel.obtainState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PhotoFragmentViewState) {
        viewModel.getLocallySavedData(PhotoFragmentEvent.loadLocallySaved)
        binding.pullToRefresh.isRefreshing = false
        state.loadingStateVisibility?.let {
            lifecycleScope.launch {
                //   binding.progressView.visibility = it

                if (it == View.VISIBLE) {
                    Log.e("show ", "visible $it")
                    binding.pullToRefresh.isRefreshing = true
                } else {
                    Log.e("gone ", "Gone $it")
                    binding.pullToRefresh.isRefreshing = false
                }
            }
        }
        lifecycleScope.launch {
            state.page?.let {
                photosAdapter.submitData(it)
                photosAdapter.addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading
                        || loadState.source.append is LoadState.NotLoading
                        && photosAdapter.itemCount < 1) {
                        Log.e("photosAdapter count ", " ${photosAdapter.itemCount}")
                        if (photosAdapter.itemCount == 0)
                            render( PhotoFragmentViewState(
                                null, mutableListOf(), R.string.empty_list, null,
                                null, View.VISIBLE))
                    } else {
                        render( PhotoFragmentViewState(
                            null, mutableListOf(), R.string.empty_list, null,
                            null, View.GONE))
                    }
                }
            }
        }
        state.errorStateVisibility?.let {
            binding.pagingErrorMsg.visibility = it
            binding.retryButton.visibility = it
            binding.errorSection.visibility = it
            state.errorMessage?.let {
                binding.pagingErrorMsg.text = state.errorMessage
            }
            state.errorMessageResource?.let {
                binding.pagingErrorMsg.text = getString(state.errorMessageResource)
            }
        }

    }

    override fun onRefresh() {
        fetchPhotoList()
    }

    override fun onItemClick(item: PhotoResponse) {
        // TODO: implement click
        //viewModel.event(Event.ListItemClicked(item))
    }

}
