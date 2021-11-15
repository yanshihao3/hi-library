package com.zq.ui.cityselector;

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.*
import android.widget.CheckedTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.zq.hilibrary.util.DisplayUtil
import com.zq.hilibrary.util.HiRes
import com.zq.ui.R
import com.zq.ui.databinding.DialogCitySelectorBinding
import com.zq.ui.recycler.RVViewHolder
import com.zq.ui.tab.top.TabTopInfo

/**
 * 城市选择器组件面板
 */

class CitySelectorDialogFragment : DialogFragment() {

    private var citySelectListener: onCitySelectListener? = null

    private var binding: DialogCitySelectorBinding? = null

    //用来记录我们本次，或者是之前它已经选择一次了，又传递了进啦
    //包含已选择省，市，区
    private lateinit var province: Province
    private var dataSets: List<Province>? = null
    private val defaultColor = HiRes.getColor(R.color.color_333)
    private val selectColor = HiRes.getColor(R.color.color_dd2)
    private val pleasePickStr = HiRes.getString(R.string.city_selector_tab_hint)

    //tabLayout 的选中项
    private var topTabSelectIndex = 0

    companion object {
        private const val KEY_PARAMS_DATA_SET = "key_data_set"
        private const val KEY_PARAMS_DATA_SELECT = "key_data_select"

        private const val TAB_PROVINCE = 0
        private const val TAB_CITY = 1
        private const val TAB_DISTRICT = 2
        fun newInstance(province: Province?, list: List<Province>): CitySelectorDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_PARAMS_DATA_SELECT, province)
            args.putParcelableArrayList(KEY_PARAMS_DATA_SET, ArrayList(list))
            val fragment = CitySelectorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_city_selector,
            window?.findViewById(android.R.id.content) ?: container,
            false
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (DisplayUtil.getDisplayHeightInPx(inflater.context) * 0.6f).toInt()
        )
        window?.setGravity(Gravity.BOTTOM)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.close.setOnClickListener { dismiss() }

        this.province = arguments?.getParcelable(KEY_PARAMS_DATA_SELECT) ?: Province()
        this.dataSets = arguments?.getParcelableArrayList(KEY_PARAMS_DATA_SET)
        requireNotNull(dataSets) { "params dataSets cannot be null" }


        refreshTabLayoutCount()
        binding!!.tabLayout.addTabSelectedChangeListener { index, preInfo, nextInfo ->
            //tablayout 选中的第2个，viewpager 可能还处于第一个1.则同步viewpager的页选中项
            if (binding!!.viewPager.currentItem != index) {
                binding!!.viewPager.setCurrentItem(index, false)
            }
        }
        binding!!.viewPager.addOnPageChangeListener(object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position != topTabSelectIndex) {
                    //去通知 toptablayout 进行标签的切换
                    binding!!.tabLayout.defaultSelected(topTabs[position])
                    topTabSelectIndex = position
                }
            }
        })

        binding!!.viewPager.adapter = CityPagerAdapter { tabIndex, selectDistrict ->/*点击回调*/
            //tabIndex 代表就是哪一个列表 发生了点击事件
            //selectDistrict 是代表该页面选中的数据对象(省市区)
            when (selectDistrict.type) {
                TYPE_PROVINCE -> {
                    province = selectDistrict as Province
                }
                TYPE_CITY -> {
                    province.selectCity = selectDistrict as City
                }
                TYPE_DISTRICT -> {
                    province.selectDistrict = selectDistrict
                }
            }
            //如果说本次选中的数据对象不是区的类型。 是省市类型，三级选择还未完成
            if (!TextUtils.equals(selectDistrict.type, TYPE_DISTRICT)) {
                refreshTabLayoutCount()
            } else {//三级选择完成
                citySelectListener?.onCitySelect(province)
                dismiss()
            }
        }

    }


    inner class CityPagerAdapter(private val itemClickCallback: (Int, District) -> Unit) :
        PagerAdapter() {
        private val views = SparseArray<CityListView>(3)
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            //1. 需要完成对应页面的view 的创建
            val view = views.get(position) ?: CityListView(container.context)
            views.put(position, view)//把页面的position 和 view绑定关联存储
            //2. 给这个view设定数据,这个数据怎么来 position =0（省份页面）
            val select: District?//3. 找出每一个页面 position =0(省份) 它的选中项
            val list = when (position) {
                TAB_PROVINCE -> {/*第0页*/
                    select = province  //如果还未选择过，自然是没有，如果选择过了province 就不为空，就是当前页选中项了
                    dataSets/*list*/
                }
                TAB_CITY -> {
                    select = province.selectCity
                    province.cities
                }
                TAB_DISTRICT -> {
                    select = province.selectDistrict
                    province.selectCity!!.districts
                }
                else -> throw IllegalStateException("pageCount must be less than ${views.size()}")
            }
            view.setData(select, list) { selectDistrict ->
                if (binding!!.viewPager.currentItem != position) return@setData
                itemClickCallback(position, selectDistrict)
            }
            if (view.parent == null) container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(views[position])
        }

        override fun getItemPosition(`object`: Any): Int {
            //需要根据object ,其实就是 instantiateItem返回的值，也就是cityListView
            //来判断它是第几个页面
            //第一个页面省份页面不会改变，没必要刷新
            return if (views.indexOfValue(`object` as CityListView?) > 0) POSITION_NONE else POSITION_UNCHANGED
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return topTabs.size
        }

    }


    inner class CityListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : RecyclerView(context, attrs, defStyleAttr) {
        private lateinit var onItemClick: (District) -> Unit
        private var selectDistrict: District? = null
        private var districtList = ArrayList<District>()
        private var lastSelectIndex = -1
        private var currentSelectIndex = -1
        fun setData(select: District?, list: List<District>?, onItemClick: (District) -> Unit) {
            if (list.isNullOrEmpty()) return
            lastSelectIndex = -1
            currentSelectIndex = -1
            this.onItemClick = onItemClick
            selectDistrict = select
            districtList.clear()
            districtList.addAll(list)
            //Cannot call this method while RecyclerView is computing a layout or scrolling
            //recyclerview 在布局阶段 或者滑动阶段 不可以调用notifyDataSetChanged
            //这不是必现的，经过我们的测试，是在viewpager刷新的时候，新增了页面，新创建了recyclerview 。
            // 由于recyclerview可能还没有布局完成，我们进击者调用notify
            post { adapter?.notifyDataSetChanged() }
        }

        init {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = object : RecyclerView.Adapter<RVViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
                    return RVViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.dialog_city_selector_list_item, parent, false)
                    )
                }

                override fun getItemCount(): Int {
                    return districtList.size
                }

                override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
                    val checkedTextView = holder.findViewById<CheckedTextView>(R.id.title)
                    val district = districtList[position]
                    checkedTextView?.text = district.districtName

                    holder.itemView.setOnClickListener {
                        selectDistrict = district
                        currentSelectIndex = position
                        notifyItemChanged(lastSelectIndex)/*变更先前选中item的样式*/
                        notifyItemChanged(position)
                    }

                    //点击之后触发刷新，说明当前item 是本次选中项
                    if (currentSelectIndex == position && currentSelectIndex != lastSelectIndex) {
                        onItemClick(district)
                    }

                    //首次进入或者点击之后的刷新Item 状态的正确性
                    if (selectDistrict?.id == district.id) {
                        currentSelectIndex = position
                        lastSelectIndex = position
                    }

                    //改变item的状态了
                    checkedTextView?.isChecked = currentSelectIndex == position
                }

            }
        }
    }

    //根据 province  更新tablayout的标签的数据
    //province--->拉起选择器的时候 传递过来的。
    //province--->本次拉起选择器的每一次选择，都会记录到province
    //每一次选择都会调用该方法 更新tablayout的个数

    private val topTabs = mutableListOf<TabTopInfo<Int>>()
    private fun refreshTabLayoutCount() {
        topTabs.clear()
        //要不要给添加一个请选择的 tab标签
        var addPleasePickTab = true

        //构建省tab
        if (!TextUtils.isEmpty(province.id)) {
            topTabs.add(newTabTopInfo(province.districtName))
        }

        //构建市tab
        if (province.selectCity != null) {
            topTabs.add(newTabTopInfo(province.selectCity!!.districtName))
        }

        //构建区tab
        if (province.selectDistrict != null) {
            topTabs.add(newTabTopInfo(province.selectDistrict!!.districtName))
            addPleasePickTab = false
            /*之前选过省市区，切回填完*/
        }

        if (addPleasePickTab) {/*之前没选过省市区*/
            topTabs.add(newTabTopInfo(pleasePickStr))
        }

        binding!!.viewPager.adapter?.notifyDataSetChanged()

        //notifyDataSetChanged它是异步
        // inflateInfo，defaultSelected是同步，就会触发addTabSelectedChangeListener，进而触发viewpager.setCurrenItem
        // 如果viewpager 还没刷新完成， 还没有从1页变成2页，此时肯定会报错
        binding!!.tabLayout.post {
            binding!!.tabLayout.inflateInfo(topTabs as List<TabTopInfo<*>>)
            //场景是 addPleasePickTab =true ,省市区还没选择完，此时需要选择最后一个tab
            // addPleasePickTab=false.,省市区都已经选择完了，就发生在第二次进入
            binding!!.tabLayout.defaultSelected(topTabs[if (addPleasePickTab) topTabs.size - 1 else 0])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun newTabTopInfo(districtName: String?) =
        TabTopInfo(districtName, defaultColor, selectColor)

    fun setCitySelectListener(listener: onCitySelectListener) {
        this.citySelectListener = listener
    }

    interface onCitySelectListener {
        fun onCitySelect(province: Province)
    }
}
