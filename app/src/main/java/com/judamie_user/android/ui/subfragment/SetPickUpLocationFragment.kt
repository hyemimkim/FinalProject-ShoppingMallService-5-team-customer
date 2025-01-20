package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentSetPickUpLocationBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.SetPickUpLocationViewModel


class SetPickUpLocationFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentSetPickUpLocationBinding: FragmentSetPickUpLocationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSetPickUpLocationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_set_pick_up_location,container,false)
        fragmentSetPickUpLocationBinding.setPickUpLocationViewModel = SetPickUpLocationViewModel(this)
        fragmentSetPickUpLocationBinding.lifecycleOwner = this

        fragmentSetPickUpLocationBinding.apply {
            materialToolbarSetPickupLocation.setOnMenuItemClickListener {
                when(it.itemId){

                    R.id.menuItemGettingMyLocation -> {
                        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_PICKUP_LOCATION_DIALOG_FRAGMENT,true,true,null)
                    }
                }
                true
            }
        }


        return fragmentSetPickUpLocationBinding.root
    }

    fun movePrevFragment() {

    }


}