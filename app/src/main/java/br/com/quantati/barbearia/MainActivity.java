package br.com.quantati.barbearia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.com.quantati.barbearia.adapter.AgendamentoAdapter;
import br.com.quantati.barbearia.dao.AgendamentoDAO;
import br.com.quantati.barbearia.model.Agendamento;

/**
 * Created by carlos on 29/05/17.
 */

public class MainActivity extends AppCompatActivity {

    private ListView listaAgendamento;
    private AgendamentoAdapter adapter;
    private List<Agendamento> agendamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaAgendamento = (ListView) findViewById(R.id.listaAgendamento);
        listaAgendamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agendamento agendamentoSelecionado = adapter.getItem(position);
                Intent cadAgendamento = new Intent(MainActivity.this, AgendamentoActivity.class);
                cadAgendamento.putExtra("agendamentoSelecionado", agendamentoSelecionado);
                startActivity(cadAgendamento);
            }
        });
        Button btnNovoAgendamento = (Button) findViewById(R.id.novoAgendamento);
        btnNovoAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgendamentoActivity.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(listaAgendamento);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    private void carregaLista() {
        AgendamentoDAO dao = new AgendamentoDAO(this);
        agendamentos = dao.list();
        dao.close();

        adapter = new AgendamentoAdapter(this, agendamentos);
        listaAgendamento.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Agendamento agendamentoSelecionado = adapter.getItem(info.position);
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                confirmaRemocao(agendamentoSelecionado);
                return false;
            }
        });
    }

    private void confirmaRemocao(final Agendamento agendamentoSelecionado) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Remoção");
        dialogBuilder.setMessage(
                String.format("Confirma a remoção do agendamento do cliente %s?",
                        agendamentoSelecionado.getNome()));
        dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AgendamentoDAO dao = new AgendamentoDAO(MainActivity.this);
                dao.delete(agendamentoSelecionado.getId());
                dao.close();
                carregaLista();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        dialogBuilder.create().show();
    }

}

