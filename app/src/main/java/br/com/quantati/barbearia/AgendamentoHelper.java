package br.com.quantati.barbearia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.quantati.barbearia.dao.AgendamentoDAO;
import br.com.quantati.barbearia.model.Agendamento;
import br.com.quantati.barbearia.model.Procedimento;


/**
 * Created by Carlos on 29/05/2017.
 */

public class AgendamentoHelper {

    private AgendamentoActivity activity;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private EditText edtNome, edtTelefone, edtDataHora;
    private Button btnSalvarAgendamento, btnFoto;
    private ViewPager foto;

    private Spinner spinnerProcedimento;
    private List<Procedimento> procedimento;
    private ArrayAdapter<Procedimento> adapter;
    private Procedimento procedimentoSelecionado;

    private Agendamento agendamento;

    public AgendamentoHelper(final AgendamentoActivity activity){
        this.activity = activity;
        procedimento = Arrays.asList(Procedimento.values());
        adapter = new ArrayAdapter<Procedimento>(activity, android.R.layout.simple_list_item_1, procedimento);
        //viewPagerAdapter = new ViewPagerAdapter(activity.getApplicationContext());

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

        foto = (ViewPager) activity.findViewById(R.id.foto);
        //foto.setAdapter(viewPagerAdapter);

        btnFoto = (Button) activity.findViewById(R.id.formFotoButton);
        agendamento = (Agendamento) activity.getIntent().getSerializableExtra("agendamentoSelecionado");
        if(agendamento != null){
            carregaDadosParaTela(agendamento);
        } else {
            agendamento = new Agendamento();
        }
    }


    public Agendamento carregaDadosDaTela() {
        agendamento.setNome(edtNome.getText().toString());
        agendamento.setTelefone(edtTelefone.getText().toString());
        if(agendamento.getFotoAntes() == null || agendamento.getFotoAntes() == foto.getTag()) {
            agendamento.setFotoAntes((String) foto.getTag());
        } else {
            agendamento.setFotoDepois((String) foto.getTag());
        }
        agendamento.setDataHora(getDate());
        agendamento.setProcedimento(procedimentoSelecionado);
        return agendamento;
    }

    public void carregaDadosParaTela(Agendamento agendamento) {
        this.agendamento = agendamento;
        edtNome.setText(agendamento.getNome());
        edtTelefone.setText(agendamento.getTelefone());
        setImage(agendamento.getFotoAntes());
        setImage(agendamento.getFotoDepois());
        setDate(agendamento.getDataHora());
        spinnerProcedimento.setSelection(procedimento.indexOf(agendamento.getProcedimento()));

        edtNome.setEnabled(false);
        edtTelefone.setEnabled(false);
        spinnerProcedimento.setEnabled(false);
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

    private Bitmap fotoAntes, fotoDepois;

    public void setImage(String localArquivoFoto) {
        if (localArquivoFoto != null) {
            foto.setTag(localArquivoFoto);
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.person);
            foto.setAdapter(null);

            Bitmap fotoAtual = BitmapFactory.decodeFile(localArquivoFoto);

            if (fotoAntes == null) {
                fotoAntes = fotoAtual;
                //viewPagerAdapter.setImage(fotoAntes, icon);
            } else {
                fotoDepois = fotoAtual;
                //viewPagerAdapter.setImage(fotoAntes, fotoDepois);
            }

            //viewPagerAdapter.notifyDataSetChanged();
            //foto.setAdapter(viewPagerAdapter);
        }
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
        Calendar cal = new GregorianCalendar(year, month, day);

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



}
