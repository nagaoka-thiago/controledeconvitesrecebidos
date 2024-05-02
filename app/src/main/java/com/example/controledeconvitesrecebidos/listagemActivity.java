package com.example.controledeconvitesrecebidos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class listagemActivity extends AppCompatActivity {

    private ListView lstConvite;
    private List<Convite> convites;
    private ConviteAdapter adapter;

    public static final int CADASTRAR_CONVITE = 1;
    public static final int EDITAR_CONVITE = 2;
    private static final String ARQUIVO = "com.example.controledeconvitesrecebidos.PREFERENCES";
    private static final int TITULO = 1;
    private static final int LOCAL = 2;
    private static final int PRECO = 3;
    private static final String ORDENACAO = "ORDENACAO";

    private View viewSelecionada;
    private int posicaoSelecionada = -1;
    private ActionMode actionMode;
    private int ordenacaoSelecionada = TITULO;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflate = actionMode.getMenuInflater();
            inflate.inflate(R.menu.listview_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.miEditar:
                    alterarConvite();
                    mode.finish();
                    return true;
                case R.id.miExcluir:
                    excluirConvite();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null) {
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            viewSelecionada = null;

            lstConvite.setEnabled(true);
        }
    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_listagem);

            lstConvite = findViewById(R.id.lstConvites);

            pegarTodos();

            lstConvite.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            lstConvite.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (actionMode != null) {
                        return false;
                    }

                    posicaoSelecionada = i;
                    view.setBackgroundColor(Color.LTGRAY);

                    viewSelecionada = view;

                    lstConvite.setEnabled(false);

                    actionMode = startSupportActionMode(mActionModeCallback);

                    return true;
                }
            });

            lerPreferencias();
            popularListView();
            ordenar();


        }

        private void ordenar() {
            switch(ordenacaoSelecionada) {
                case TITULO:
                    Collections.sort(convites, new Comparator<Convite>() {
                        @Override
                        public int compare(Convite c1, Convite c2) {
                            return c1.getTitulo().compareTo(c2.getTitulo());
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
                case LOCAL:
                    Collections.sort(convites, new Comparator<Convite>() {
                        @Override
                        public int compare(Convite c1, Convite c2) {
                            return c1.getLocal().compareTo(c2.getLocal());
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
                case PRECO:
                    ordenacaoSelecionada = PRECO;
                    Collections.sort(convites, new Comparator<Convite>() {
                        @Override
                        public int compare(Convite c1, Convite c2) {
                            return Float.compare(c1.getPreco(), c2.getPreco());
                        }
                    });
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        private void lerPreferencias() {
            SharedPreferences shared = getSharedPreferences(ARQUIVO, MODE_PRIVATE);
            ordenacaoSelecionada = shared.getInt(ORDENACAO, ordenacaoSelecionada);
        }

        private void escreverPreferencias(int novoValor) {
            SharedPreferences shared = getSharedPreferences(ARQUIVO, MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();

            editor.putInt(ORDENACAO, novoValor);

            editor.commit();

            ordenacaoSelecionada = novoValor;

            ordenar();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.listagem_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.miAdicionar:
                    botaoAdicionarClicado();
                    return true;
                case R.id.miSobre:
                    botaoSobreClicado();
                    return true;
                case R.id.miTitulo:
                    escreverPreferencias(TITULO);
                    item.setChecked(true);
                    return true;
                case R.id.miLocal:
                    escreverPreferencias(LOCAL);
                    item.setChecked(true);
                    return true;
                case R.id.miPreco:
                    escreverPreferencias(PRECO);
                    item.setChecked(true);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            MenuItem item;
            switch(ordenacaoSelecionada) {
                case TITULO:
                    item = menu.findItem(R.id.miTitulo);
                    break;
                case LOCAL:
                    item = menu.findItem(R.id.miLocal);
                    break;
                case PRECO:
                    item = menu.findItem(R.id.miPreco);
                    break;
                default:
                    return super.onPrepareOptionsMenu(menu);
            }
            item.setChecked(true);
            return true;
    }

    private void popularListView() {
            adapter = new ConviteAdapter(this, convites);
            lstConvite.setAdapter(adapter);
        }

        private void botaoSobreClicado() {
            Intent intent = new Intent(this, AutoriaActivity.class);

            startActivity(intent);
        }

        private void botaoAdicionarClicado() {
            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra(MainActivity.MODO, CADASTRAR_CONVITE);

            startActivityForResult(intent, CADASTRAR_CONVITE);
        }

        private void alterarConvite() {
            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra(MainActivity.MODO, EDITAR_CONVITE);
            intent.putExtra(MainActivity.DADO, convites.get(posicaoSelecionada).getId());

            startActivityForResult(intent, EDITAR_CONVITE);
        }

        private void excluirConvite() {

            UtilsGUI.confirmaAcao(this, getString(R.string.confirmar_deletar_convite, convites.get(posicaoSelecionada).getTitulo()), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int botaoSelecionado) {
                    switch(botaoSelecionado) {
                        case DialogInterface.BUTTON_POSITIVE:
                            ConvitesDatabase database = ConvitesDatabase.getDatabase(getApplicationContext());

                            database.conviteDAO().deletar(convites.get(posicaoSelecionada));
                            convites.remove(posicaoSelecionada);

                            ordenar();

                            adapter.notifyDataSetChanged();
                            break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                    }
                }
            });
        }

        private void pegarTodos() {
            ConvitesDatabase database = ConvitesDatabase.getDatabase(this);

            convites = database.conviteDAO().pegarTodos();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == CADASTRAR_CONVITE && resultCode == Activity.RESULT_OK) {
                pegarTodos();

                ordenar();

                popularListView();
            } else if (requestCode == EDITAR_CONVITE && resultCode == Activity.RESULT_OK) {
                pegarTodos();

                ordenar();

                popularListView();
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }