package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.servicosagenda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalendario extends Fragment {

    // TODO: Definindo parametros
    private CalendarView calendarView;

    public FragmentCalendario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarWiew_Calendario);
        return view;
    }
}