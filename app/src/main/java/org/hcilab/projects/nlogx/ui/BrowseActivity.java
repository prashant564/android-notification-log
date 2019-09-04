package org.hcilab.projects.nlogx.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.hcilab.projects.nlogx.R;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private BrowseAdapter adapter;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView = findViewById(R.id.list);
		recyclerView.setLayoutManager(layoutManager);

		swipeRefreshLayout = findViewById(R.id.swiper);
		swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
		swipeRefreshLayout.setOnRefreshListener(this);

		update();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && DetailsActivity.ACTION_REFRESH.equals(data.getStringExtra(DetailsActivity.EXTRA_ACTION))) {

			update();
		}
	}



	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				update();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void update() {



		adapter = new BrowseAdapter(this);

		recyclerView.setAdapter(adapter);

		if(adapter.getItemCount() == 0) {
			Toast.makeText(this, R.string.empty_log_file, Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void onRefresh() {
		update();
		swipeRefreshLayout.setRefreshing(false);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browse, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView) searchItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

               adapter.getFilter().filter(newText);

				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

}