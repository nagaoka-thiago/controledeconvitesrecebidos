package com.example.controledeconvitesrecebidos;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ImageView imgConvite;
    private EditText etTitulo;
    private EditText etLocal;
    private EditText etData;
    private EditText etPreco;
    private RadioGroup rgHomensMulheres;
    private CheckBox cbBebidaLiberada;
    private Spinner spnEstado;
    private Button btnTirarFoto;
    private Convite conviteAtualizar;
    private String[] estados = new String[]{
        "Acre",
        "Alagoas",
        "Amapá",
        "Amazonas",
        "Bahia",
        "Ceará",
        "Espírito Santo",
        "Goiás",
        "Maranhão",
        "Mato Grosso",
        "Mato Grosso do Sul",
        "Minas Gerais",
        "Pará",
        "Paraíba",
        "Paraná",
        "Pernambuco",
        "Piauí",
        "Rio de Janeiro",
        "Rio Grande do Norte",
        "Rio Grande do Sul",
        "Rondônia",
        "Roraima",
        "Santa Catarina",
        "São Paulo",
        "Sergipe",
        "Tocantins"
    };

    private int modo;

    public static final String MODO = "MODO";
    public static final String DADO = "DADO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        imgConvite = findViewById(R.id.imgConvite);
        etTitulo = findViewById(R.id.etTitulo);
        etLocal = findViewById(R.id.etLocal);
        etData = findViewById(R.id.etData);
        etPreco = findViewById(R.id.etPreco);
        rgHomensMulheres = findViewById(R.id.rgHomensMulheres);
        cbBebidaLiberada = findViewById(R.id.cbBebidaLiberada);
        spnEstado = findViewById(R.id.spnEstado);
        btnTirarFoto = findViewById(R.id.btnTirarFoto);
        btnTirarFoto.setOnClickListener(this::tirarFoto);

        popularEstados();

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            modo = bundle.getInt(MODO, 0);

            if(modo == listagemActivity.EDITAR_CONVITE) {
                long id = bundle.getLong(DADO);
                ConvitesDatabase database = ConvitesDatabase.getDatabase(this);

                conviteAtualizar = database.conviteDAO().pegarPorId(id);
                popularConviteAtualizar();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSalvar:
                salvar();
                return true;
            case R.id.miLimpar:
                limpar();
                return true;
            case android.R.id.home:
                finalizar(true, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popularEstados() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        spnEstado.setAdapter(adapter);
    }

    private void popularConviteAtualizar() {
        imgConvite.setImageDrawable(
                new BitmapDrawable(BitmapFactory.decodeByteArray(
                        conviteAtualizar.getImgConvite(),
                        0,
                        conviteAtualizar.getImgConvite().length)));
        etTitulo.setText(conviteAtualizar.getTitulo());
        etLocal.setText(conviteAtualizar.getLocal());
        etData.setText(conviteAtualizar.getData());
        etPreco.setText(String.valueOf(conviteAtualizar.getPreco()));
        ((RadioButton)rgHomensMulheres.getChildAt(conviteAtualizar.getHomensMulheres().ordinal())).setChecked(true);
        cbBebidaLiberada.setChecked(conviteAtualizar.isBebidaLiberada());

        for(int i = 0; i < estados.length; i++) {
            if(estados[i].compareTo(conviteAtualizar.getEstado()) == 0) {
                spnEstado.setSelection(i);
                break;
            }
        }
    }

    private void limpar() {
        imgConvite.setImageBitmap(null);
        etTitulo.setText(null);
        etLocal.setText(null);
        etData.setText(null);
        etPreco.setText(null);
        rgHomensMulheres.clearCheck();
        cbBebidaLiberada.setChecked(false);
        spnEstado.setSelection(0);


    }

    private void salvar() {
        if(imgConvite.getDrawable() == null) {
            UtilsGUI.avisoErro(this, R.string.precisa_foto, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            return;
        }
        else if(etTitulo.getText().length() == 0) {
            UtilsGUI.avisoErro(this, R.string.precisa_titulo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            etTitulo.requestFocus();
            return;
        }
        else if(etLocal.getText().length() == 0) {
            UtilsGUI.avisoErro(this, R.string.precisa_local, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            etLocal.requestFocus();
            return;
        }
        else if(etData.getText().length() == 0) {
            UtilsGUI.avisoErro(this, R.string.precisa_data, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            etData.requestFocus();
            return;
        }
        else if(etPreco.getText().length() == 0) {
            UtilsGUI.avisoErro(this, R.string.precisa_preco, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            etPreco.requestFocus();
            return;
        }
        else if(rgHomensMulheres.getCheckedRadioButtonId() == -1) {
            UtilsGUI.avisoErro(this, R.string.precisa_selecionar_homens_mulheres, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            return;
        }
        else if(spnEstado.getSelectedItemPosition() == -1) {
            UtilsGUI.avisoErro(this, R.string.precisa_selecionar_estado, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            return;
        }
        else {
            UtilsGUI.avisoErro(this, R.string.convite_validado, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    HomensMulheres homensMulheres = HomensMulheres.HOMENS_E_MULHERES;
                    switch(rgHomensMulheres.getCheckedRadioButtonId()) {
                        case R.id.rbMulheres:
                            homensMulheres = HomensMulheres.MULHERES;
                            break;
                        case R.id.rbHomens:
                            homensMulheres = HomensMulheres.HOMENS;
                            break;
                        case R.id.rbAmbos:
                            homensMulheres = HomensMulheres.HOMENS_E_MULHERES;
                            break;
                    }


                    if(modo == listagemActivity.CADASTRAR_CONVITE) {
                        Convite convite = new Convite(converteDrawableParaBytes(imgConvite.getDrawable()),
                                etTitulo.getText().toString(),
                                etLocal.getText().toString(),
                                etData.getText().toString(),
                                Float.parseFloat(etPreco.getText().toString()),
                                homensMulheres,
                                cbBebidaLiberada.isChecked(),
                                spnEstado.getSelectedItem().toString());

                        ConvitesDatabase database = ConvitesDatabase.getDatabase(getApplicationContext());

                        long id = database.conviteDAO().inserir(convite);
                        convite.setId(id);
                        finalizar(false, convite);
                    }
                    else if(modo == listagemActivity.EDITAR_CONVITE) {
                        conviteAtualizar.setImgConvite(converteDrawableParaBytes(imgConvite.getDrawable()));
                        conviteAtualizar.setData(etData.getText().toString());
                        conviteAtualizar.setEstado(spnEstado.getSelectedItem().toString());
                        conviteAtualizar.setHomensMulheres(homensMulheres);
                        conviteAtualizar.setLocal(etLocal.getText().toString());
                        conviteAtualizar.setIsBebidaLiberada(cbBebidaLiberada.isChecked());
                        conviteAtualizar.setPreco(Float.parseFloat(etPreco.getText().toString()));
                        conviteAtualizar.setTitulo(etTitulo.getText().toString());

                        ConvitesDatabase database = ConvitesDatabase.getDatabase(getApplicationContext());

                        database.conviteDAO().atualizar(conviteAtualizar);

                        finalizar(false, conviteAtualizar);
                    }
                }
            });
        }
    }

    public void tirarFoto(View view) {
        Intent tirarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tirarFotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(tirarFotoIntent, 1);
        }
    }

    private byte[] converteDrawableParaBytes(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imgConvite.setImageBitmap(image);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void finalizar(boolean isBack, Convite convite) {

        if(modo == listagemActivity.CADASTRAR_CONVITE || modo == listagemActivity.EDITAR_CONVITE) {
            Intent intent = new Intent();
            if(isBack) {

                setResult(Activity.RESULT_CANCELED, intent);
            }
            else {
                setResult(Activity.RESULT_OK, intent);
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
      finalizar(true, null);
    }
}