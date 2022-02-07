/*
 *  Nextcloud Android Library is available under MIT license
 *
 *  @author Álvaro Brey Vilas
 *  Copyright (C) 2022 Álvaro Brey Vilas
 *  Copyright (C) 2022 Nextcloud GmbH
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *  ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.owncloud.android.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nextcloud.client.di.Injectable
import com.nextcloud.client.di.ViewModelFactory
import com.owncloud.android.databinding.PreviewPdfFragmentBinding
import com.owncloud.android.datamodel.OCFile
import javax.inject.Inject

class PreviewPdfFragment : Fragment(), Injectable {

    @Inject
    lateinit var vmFactory: ViewModelFactory

    private lateinit var binding: PreviewPdfFragmentBinding
    private lateinit var viewModel: PreviewPdfViewModel

    companion object {
        private const val ARG_FILE = "FILE"

        @JvmStatic
        fun newInstance(file: OCFile): PreviewPdfFragment = PreviewPdfFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_FILE, file)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PreviewPdfFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pdfRenderer.observe(viewLifecycleOwner) { renderer ->
            binding.pdfRecycler.adapter = PreviewPdfAdapter(renderer, getScreenWidth())
        }
        val file: OCFile = requireArguments().getParcelable(ARG_FILE)!!
        viewModel.process(file)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory)[PreviewPdfViewModel::class.java]
    }

    private fun getScreenWidth(): Int =
        requireContext().resources.displayMetrics.widthPixels
}
