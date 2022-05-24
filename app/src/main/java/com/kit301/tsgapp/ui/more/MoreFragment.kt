package com.kit301.tsgapp.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kit301.tsgapp.FavouriteProduct
import com.kit301.tsgapp.R
import com.kit301.tsgapp.TakePhoto
import com.kit301.tsgapp.databinding.FragmentHomeBinding
import com.kit301.tsgapp.databinding.FragmentMoreBinding
import com.kit301.tsgapp.ui.PromotionActivity

class MoreFragment : Fragment() {

    private lateinit var moreViewModel: MoreViewModel

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moreViewModel =
            ViewModelProvider(this).get(MoreViewModel::class.java)
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = root.findViewById(R.id.text_more)
        moreViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        binding.textPromotion.setOnClickListener {
            val i = Intent(context, PromotionActivity::class.java)
            startActivity(i)
        }

        binding.textFavorite.setOnClickListener {
            val i = Intent(context, FavouriteProduct::class.java)
            startActivity(i)
        }


        return root
    }
}