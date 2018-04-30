package br.com.quantati.barbearia;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.io.File;

import br.com.quantati.barbearia.util.ImageFilePath;
import br.com.quantati.barbearia.util.ImageUtil;

/**
 * Created by Carlos on 29/05/2017.
 */

public class AgendamentoActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        ViewPagerEx.OnPageChangeListener {

    private AgendamentoHelper helper;
    private static final int TIRAR_FOTO = 1;
    private static final int ESCOLHER_FOTO = 2;
    private String localArquivoFoto;
    private SliderLayout sliderFotos;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);
        helper = new AgendamentoHelper(this);
        Button btnFoto = helper.getBtnFoto();
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.setPageAtual(page);
                v.showContextMenu();
            }
        });
        sliderFotos = helper.getSliderFotos();

        registerForContextMenu(btnFoto);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem biblioteca = menu.add("Procurar na biblioteca");
        biblioteca.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), ESCOLHER_FOTO);
                return true;
            }
        });
        MenuItem camera = menu.add("Tirar Foto");
        camera.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".png";
                Intent intentCamera = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localArquivoFoto)));
                startActivityForResult(intentCamera, TIRAR_FOTO);
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ESCOLHER_FOTO) {
                localArquivoFoto = ImageFilePath.getPath(this.getApplicationContext(), data.getData());
                helper.setImage(localArquivoFoto);
            } else if (requestCode == TIRAR_FOTO) {
                helper.setImage(localArquivoFoto);
            } else {
                localArquivoFoto = null;
            }
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        helper.onDateSet(view, year, month, day);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        helper.onTimeSet(view, hourOfDay, minute);
    }

    @Override
    public void onStop() {
        sliderFotos.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        page = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
