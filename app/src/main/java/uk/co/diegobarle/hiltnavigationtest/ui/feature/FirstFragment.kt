package uk.co.diegobarle.hiltnavigationtest.ui.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.co.diegobarle.hiltnavigationtest.R

@AndroidEntryPoint
class FirstFragment : Fragment() {

    /**
     * TODO here is where the issue lies.
     * Using viewModels() doesn't throw the exception '<view> does not have a NavController set'
     * and the ViewModel is created and injected properly.
     */
    private val viewModel: FeatureSharedViewModel by hiltNavGraphViewModels(R.id.feature_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<EditText>(R.id.et1).setText(viewModel.sharedValue)

        view.findViewById<Button>(R.id.btn1).setOnClickListener {
            val value = view.findViewById<EditText>(R.id.et1).text.toString()
            viewModel.navigateToSecondScreen(value)
        }
    }
}