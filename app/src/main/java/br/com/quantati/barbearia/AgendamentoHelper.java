package br.com.quantati.barbearia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.quantati.barbearia.dao.AgendamentoDAO;
import br.com.quantati.barbearia.model.Agendamento;
import br.com.quantati.barbearia.model.Procedimento;


/**
 * Created by Carlos on 29/05/2017.
 */

public class AgendamentoHelper {

    private static final int DELAY = 10000;
    private static final int LIMITE_FOTOS = 2;
    private AgendamentoActivity activity;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private EditText edtNome, edtTelefone, edtDataHora;
    private Button btnSalvarAgendamento, btnFoto;

    private Spinner spinnerProcedimento;
    private List<Procedimento> procedimento;
    private ArrayAdapter<Procedimento> adapter;
    private Procedimento procedimentoSelecionado;

    private Agendamento agendamento;


    // slider de fotos
    private SliderLayout sliderFotos;
    private Map<Integer,String> fotos = new TreeMap<>();
    private int pageAtual;
    private int qtdeFoto;

    public AgendamentoHelper(final AgendamentoActivity activity){
        this.activity = activity;
        procedimento = Arrays.asList(Procedimento.values());
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, procedimento);

        sliderFotos = (SliderLayout) activity.findViewById(R.id.sliderFotos);
        sliderFotos.setDuration(DELAY);
        sliderFotos.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderFotos.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderFotos.addOnPageChangeListener(activity);

        edtNome = (EditText) activity.findViewById(R.id.edtNome);
        edtTelefone = (EditText) activity.findViewById(R.id.edtTelefone);
        edtDataHora = (EditText) activity.findViewById(R.id.edtDataHora);
        edtDataHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(v);
            }
        });

        spinnerProcedimento = (Spinner) activity.findViewById(R.id.spinnerProcedimento);
        spinnerProcedimento.setAdapter(adapter);
        spinnerProcedimento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                procedimentoSelecionado = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSalvarAgendamento = (Button) activity.findViewById(R.id.btnSalvarAgendamento);
        btnSalvarAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agendamento = carregaDadosDaTela();
                if(validate()){
                    AgendamentoDAO dao = new AgendamentoDAO(activity);
                    if(dao.podeAgendar(agendamento)){
                        if(agendamento.getId() == 0){
                            dao.insert(agendamento);
                        } else {
                            dao.update(agendamento);
                        }
                    } else {
                        edtDataHora.setError("Horário conflitante.");
                        return;
                    }
                    dao.close();
                    activity.finish();
                }
            }
        });

        btnFoto = (Button) activity.findViewById(R.id.formFotoButton);
        agendamento = (Agendamento) activity.getIntent().getSerializableExtra("agendamentoSelecionado");
        if(agendamento != null){
            carregaDadosParaTela(agendamento);
        } else {
            agendamento = new Agendamento();
            agendamento.setNovo(true);
            carregaCarousel(agendamento);
        }
    }

    public Agendamento carregaDadosDaTela() {
        agendamento.setNome(edtNome.getText().toString());
        agendamento.setTelefone(edtTelefone.getText().toString());
        agendamento.setDataHora(getDate());
        agendamento.setProcedimento(procedimentoSelecionado);
        return setImagens(agendamento);
    }

    private Agendamento setImagens(Agendamento agendamento) {
        agendamento.setFotoAntes(fotos.get(1).isEmpty() ? "" : fotos.get(1));
        agendamento.setFotoDepois(fotos.get(2).isEmpty() ? "" : fotos.get(2));
        return agendamento;
    }

    public void carregaDadosParaTela(Agendamento agendamento) {
        this.agendamento = agendamento;
        edtNome.setText(agendamento.getNome());
        edtTelefone.setText(agendamento.getTelefone());
        setDate(agendamento.getDataHora());
        spinnerProcedimento.setSelection(procedimento.indexOf(agendamento.getProcedimento()));
        carregaCarousel(agendamento);
    }

    public boolean validate() {
        boolean valid = true;
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("Campo nome é obrigatório!");
            valid = false;
        }

        if (edtTelefone.getText().toString().trim().isEmpty()) {
            edtTelefone.setError("Campo telefone é obrigatório!");
            valid = false;
        }

        return valid;
    }

    public Button getBtnFoto() {
        return btnFoto;
    }

    private void setDate(Calendar calendar) {
        try {
            edtDataHora.setText(format.format(calendar.getTime()));
        }
        catch (Exception e) {
            edtDataHora.setText(format.format(new Date()));
        }
    }

    private Calendar getDate() {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(edtDataHora.getText().toString()));
        }
        catch (Exception e) {
            c.setTime(new Date());
        }
        return c;
    }

    int yearFinal, monthFinal, dayFinal;

    public void onDateSet(DatePicker view, int year, int month, int day) {
        yearFinal = year;
        monthFinal = month;
        dayFinal = day;

        if (view.isShown()) {
            timePicker(view);
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        Calendar cal = new GregorianCalendar(yearFinal, monthFinal, dayFinal, hourOfDay, minute);
        setDate(cal);
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("calendar", agendamento.getDataHora());
        fragment.setArguments(args);
        fragment.show(activity.getFragmentManager(), "");
    }

    public void timePicker(View view){
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("calendar", agendamento.getDataHora());
        fragment.setArguments(args);
        fragment.show(activity.getFragmentManager(), "");
    }

    public SliderLayout getSliderFotos() {
        return sliderFotos;
    }

    public void setImage(String localArquivoFoto) {
        int position = qtdeFoto == LIMITE_FOTOS ? pageAtual : qtdeFoto;
        fotos.put(position + 1, localArquivoFoto);
        populaCarousel();
    }

    /* metódo que armazena as fotos em um map */
    private void carregaCarousel(Agendamento agendamento) {
        fotos.put(1, agendamento.getFotoAntes().isEmpty() ? "" : agendamento.getFotoAntes());
        fotos.put(2, agendamento.getFotoDepois().isEmpty() ? "" : agendamento.getFotoDepois());
        populaCarousel();
    }

    /* método que popula o carousel a partir do map com as fotos
       - qdo nao tem a foto, insere uma imagem (person) que indica nao haver fotos
     */
    private void populaCarousel() {
        sliderFotos.removeAllSliders();
        qtdeFoto = 0;
        for(int id : fotos.keySet()){
            TextSliderView textSliderView = new TextSliderView(activity);
            if (fotos.get(id).isEmpty()) {
                textSliderView.image(R.drawable.person);
            }
            else {
                textSliderView.image(new File(fotos.get(id)));
                qtdeFoto++;
            }
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            sliderFotos.addSlider(textSliderView);
        }
    }

    /* controle para saber qual a página onde foto dever ser inserida */
    public void setPageAtual(int pageAtual) {
        this.pageAtual = pageAtual;
    }
}
