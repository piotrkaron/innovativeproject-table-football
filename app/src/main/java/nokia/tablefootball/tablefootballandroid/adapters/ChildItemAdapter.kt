package nokia.tablefootball.tablefootballandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import kotlin.collections.*

class ChildItemAdapter(val context: Context, var dtosList : ArrayList<TableDTO>)
    : RecyclerView.Adapter<ChildItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cardView = inflater.inflate(R.layout.list_item, null, false)
        val viewHolder = ViewHolder(cardView)

        viewHolder.tableImageView = cardView.findViewById(R.id.table_imageview)
        viewHolder.tableStateTextView = cardView.findViewById(R.id.table_state_textview)
        viewHolder.tableRoomTextView = cardView.findViewById(R.id.table_room_textview)

        return viewHolder

    }

    override fun getItemCount(): Int {
        return dtosList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tableDto = dtosList[position]

        holder.tableImageView.setImageResource(
            when(tableDto.online){
                false -> R.mipmap.table_inactive
                true -> if(tableDto.occupied) R.mipmap.table_occupied else R.mipmap.table_free
            }
        )

        holder.tableRoomTextView.text = "Room: ${tableDto.room.toString()}"
        holder.tableStateTextView.text = if(!tableDto.online) "Inactive" else when(tableDto.occupied){
            false -> "Free"
            true -> "Occupied"
        }

    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        internal var tableImageView: ImageView = itemView.findViewById(R.id.table_imageview) as ImageView
        internal var tableStateTextView: TextView = itemView.findViewById(R.id.table_state_textview) as TextView
        internal var tableRoomTextView: TextView = itemView.findViewById(R.id.table_room_textview) as TextView

    }

}