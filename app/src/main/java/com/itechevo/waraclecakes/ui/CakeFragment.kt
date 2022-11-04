package com.itechevo.waraclecakes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.itechevo.domain.model.Cake
import com.itechevo.waraclecakes.R
import com.itechevo.waraclecakes.extensions.loadImage
import com.itechevo.waraclecakes.ui.adapter.CakeListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class CakeFragment : Fragment() {

    private lateinit var cakeListView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val cakeListAdapter: CakeListAdapter by lazy {
        CakeListAdapter { cake: Cake -> cakeItemClicked(cake) }
    }

    private val viewModel: CakeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cake, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)

        observeUiState()
    }

    private fun setupViews(view: View) {
        cakeListView = view.findViewById(R.id.cake_list)
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh)
        cakeListView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = cakeListAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCakes()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is CakeViewModel.UiState.Content -> renderUi(uiState.result)
                    CakeViewModel.UiState.Error -> showError()
                    CakeViewModel.UiState.Loading -> showProgress()
                }
            }
        }
    }

    private fun renderUi(cakeList: List<Cake>) {
        hideProgress()
        cakeListAdapter.update(cakeList)
    }

    private fun cakeItemClicked(cake: Cake) {
        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_cake_details_dialog, null)
        view.findViewById<TextView>(R.id.cake_title).apply {
            text = cake.title
        }
        view.findViewById<TextView>(R.id.cake_desc).apply {
            text = cake.description
        }
        view.findViewById<AppCompatImageView>(R.id.cake_image).apply {
            loadImage(cake.imageUrl)
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showError() {
        hideProgress()
        Snackbar
            .make(swipeRefreshLayout, getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) {
                viewModel.getCakes()
            }.show()
    }

    private fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    companion object {
        fun newInstance() = CakeFragment()
    }
}