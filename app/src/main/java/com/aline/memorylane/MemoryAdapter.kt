package com.aline.memorylane

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter that connects the list of memories to the RecyclerView
class MemoryAdapter(

    // The list of memories to show
    private var memories: List<Memory>,

    // Function that runs when a memory is clicked
    private val onMemoryClick: (Memory) -> Unit

) : RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder>() {

    // ViewHolder keeps the views for one item in the list
    class MemoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView =
            itemView.findViewById(R.id.textItemMemoryTitle)
        val category: TextView =
            itemView.findViewById(R.id.textItemMemoryCategory)
        val date: TextView =
            itemView.findViewById(R.id.textItemMemoryDate)
    }

    // Creates a new item view using the item_memory layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoryViewHolder {

        // Turn the XML layout into a real view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memory, parent, false)

        return MemoryViewHolder(view)
    }

    // Puts the memory data into the item view
    override fun onBindViewHolder(
        holder: MemoryViewHolder,
        position: Int
    ) {
        // Get the memory at this position in the list
        val memory = memories[position]

        // Show the memory info in the text views
        holder.title.text = memory.title
        holder.category.text = memory.category
        holder.date.text = memory.date

        // When the item is clicked, send the memory to the click function
        holder.itemView.setOnClickListener {
            onMemoryClick(memory)
        }
    }

    // Tells the RecyclerView how many items are in the list
    override fun getItemCount(): Int {
        return memories.size
    }

    // Replaces the list with a new one
    fun updateMemories(newMemories: List<Memory>) {
        memories = newMemories

        // Refresh the whole list on the screen
        notifyDataSetChanged()
    }
}