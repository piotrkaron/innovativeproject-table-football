package nokia.tablefootball.tablefootballandroid.activity.helpers

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import nokia.tablefootball.tablefootballandroid.utils.TableDataUtil
import java.util.*

class FloorListAdapter(private val context: Context,tableDtos: List<TableDTO>)
    : BaseExpandableListAdapter() {

    private val tablesMap: TreeMap<Int, ArrayList<TableDTO>> = TableDataUtil.toFloorMap(tableDtos)

    private val expandableListDetail = TableDataUtil.toFloorMapAsStrings(tableDtos)
    private val expandableListTitle: List<String>

    init{
        expandableListTitle = expandableListDetail.keys.toList()
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {

        return tablesMap[expandableListTitle[listPosition].toInt()]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup): View {

        var convertView = convertView

        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }

        val tableImageView = convertView!!.findViewById<ImageView>(R.id.table_imageview)
        val tableRoomTextView = convertView.findViewById<TextView>(R.id.table_room_textview)
        val tableStateTextView = convertView.findViewById<TextView>(R.id.table_state_textview)

        val tableDto = getChild(listPosition, expandedListPosition) as TableDTO

        tableImageView.setImageResource(
            when(tableDto.online){
                false -> R.mipmap.table_inactive
                true -> if(tableDto.occupied) R.mipmap.table_occupied else R.mipmap.table_free
            }
        )

        tableRoomTextView.text = tableDto.room.toString()
        tableStateTextView.text = if(!tableDto.online) tableDto.online.toString() else tableDto.occupied.toString()


        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]!!
            .size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.floor_list, null)
        }
        val listTitleTextView = convertView!!
            .findViewById<View>(R.id.listTitle) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
