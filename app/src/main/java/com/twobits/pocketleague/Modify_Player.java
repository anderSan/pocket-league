package com.twobits.pocketleague;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Edit;
import com.twobits.pocketleague.db.DatabaseCommonQueue;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;

import java.sql.SQLException;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

public class Modify_Player extends Fragment_Edit {
	Long pId;
	Player p;
	Team t;
	Dao<Player, Long> pDao;
	Dao<Team, Long> tDao;
	Dao<TeamMember, Long> tmDao;

	Button btn_create;
	TextView tv_nick;
	TextView tv_name;
	TextView tv_weight;
	TextView tv_height;
	CheckBox cb_lh;
	CheckBox cb_rh;
	CheckBox cb_lf;
	CheckBox cb_rf;
	Button btn_color;
	int player_color = Color.BLACK;
	CheckBox cb_isFavorite;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_modify_player, container, false);

        Bundle args = getArguments();
        pId = args.getLong("PID", -1);

		pDao = mData.getPlayerDao();
		tDao = mData.getTeamDao();
		tmDao = mData.getTeamMemberDao();

		btn_create = (Button) rootView.findViewById(R.id.button_createPlayer);
		tv_nick = (TextView) rootView.findViewById(R.id.editText_nickname);
		tv_name = (TextView) rootView.findViewById(R.id.editText_playerName);
		tv_weight = (TextView) rootView.findViewById(R.id.editText_weight);
		tv_height = (TextView) rootView.findViewById(R.id.editText_height);
		cb_lh = (CheckBox) rootView.findViewById(R.id.checkBox_leftHanded);
		cb_rh = (CheckBox) rootView.findViewById(R.id.checkBox_rightHanded);
		cb_lf = (CheckBox) rootView.findViewById(R.id.checkBox_leftFooted);
		cb_rf = (CheckBox) rootView.findViewById(R.id.checkBox_rightFooted);
		btn_color = (Button) rootView.findViewById(R.id.newPlayer_colorPicker);
		cb_isFavorite = (CheckBox) rootView.findViewById(R.id.newPlayer_isFavorite);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

        if (pId != -1) {
            loadPlayerValues();
        }

        return rootView;
	}

	private void loadPlayerValues() {
		try {
			p = pDao.queryForId(pId);
			t = DatabaseCommonQueue.findPlayerSoloTeam(context, p);
			btn_create.setText("Modify");
			tv_nick.setText(p.getNickName());
			tv_name.setText(p.getFirstName() + " " + p.getLastName());
			tv_weight.setText(String.valueOf(p.getWeight()));
			tv_height.setText(String.valueOf(p.getHeight()));
			cb_lh.setChecked(p.getIsLeftHanded());
			cb_rh.setChecked(p.getIsRightHanded());
			cb_lf.setChecked(p.getIsLeftFooted());
			cb_rf.setChecked(p.getIsRightFooted());
			btn_color.setBackgroundColor(p.getColor());
			player_color = p.getColor();
			cb_isFavorite.setChecked(p.getIsFavorite());
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void doneButtonPushed() {

		String nickname = tv_nick.getText().toString().trim();
		if (nickname.isEmpty()) {
			Toast.makeText(context, "Nickname is required.", Toast.LENGTH_LONG).show();
		} else {
			String[] names = tv_name.getText().toString().split("\\s+", 2);
			String first_name = names[0];
			String last_name = "";
			if (names.length >= 2) {
				last_name = names[1];
			}

			int weight_kg = 0;
			String s = tv_weight.getText().toString().trim();
			if (!s.isEmpty()) {
				weight_kg = Integer.parseInt(s);
			}

			int height_cm = 0;
			s = tv_height.getText().toString().trim();
			if (!s.isEmpty()) {
				height_cm = Integer.parseInt(s);
			}

			Boolean lh = cb_lh.isChecked();
			Boolean rh = cb_rh.isChecked();
			Boolean lf = cb_lf.isChecked();
			Boolean rf = cb_rf.isChecked();

			byte[] emptyImage = new byte[0];

			Boolean is_favorite = cb_isFavorite.isChecked();

			if (pId != -1) {
				modifyPlayer(nickname, first_name, last_name, lh, rh, lf, rf,
						height_cm, weight_kg, emptyImage, is_favorite);
			} else {
				createPlayer(nickname, first_name, last_name, lh, rh, lf, rf,
						height_cm, weight_kg, emptyImage, is_favorite);
			}
		}
	}

	private void createPlayer(String nickname, String first_name,
			String last_name, boolean lh, boolean rh, boolean lf, boolean rf,
			int height_cm, int weight_kg, byte[] image, boolean is_favorite) {

		Player newPlayer = new Player(nickname, first_name, last_name, lh, rh,
				lf, rf, height_cm, weight_kg, image, player_color, is_favorite);
		Team newTeam = new Team(nickname, 1, player_color, is_favorite);
		TeamMember newTeamMember = new TeamMember(newTeam, newPlayer);

        if (newPlayer.exists(context) || newTeam.exists(context)) {
            Toast.makeText(context, "Player already exists.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                pDao.create(newPlayer);
                tDao.create(newTeam);
                tmDao.create(newTeamMember);
                Toast.makeText(context, "Player created!", Toast.LENGTH_SHORT)
                        .show();
                mNav.onBackPressed();
            } catch (SQLException ee) {
                loge("Could not create player", ee);
                Toast.makeText(context, "Could not create player.",
                        Toast.LENGTH_SHORT).show();
            }
        }
	}

	private void modifyPlayer(String nickname, String first_name,
			String last_name, boolean lh, boolean rh, boolean lf, boolean rf,
			int height_cm, int weight_kg, byte[] image, boolean is_favorite) {

		p.setNickName(nickname);
		p.setFirstName(first_name);
		p.setLastName(last_name);
		p.setIsLeftHanded(lh);
		p.setIsRightHanded(rh);
		p.setIsLeftFooted(lf);
		p.setIsRightFooted(rf);
		p.setWeight_kg(weight_kg);
		p.setHeight_cm(height_cm);
		p.setColor(player_color);
		p.setIsFavorite(is_favorite);

		t.setTeamName(nickname);
		t.setIsFavorite(is_favorite);
		try {
			pDao.update(p);
			tDao.update(t);
			Toast.makeText(context, "Player modified.", Toast.LENGTH_SHORT).show();
			mNav.onBackPressed();
		} catch (SQLException e) {
			e.printStackTrace();
			Toast.makeText(context, "Could not modify player.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void showColorPicker(View view) {
		// initialColor is the initially-selected color to be shown in the
		// rectangle on the left of the arrow.
		// for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware
		// of the initial 0xff which is the alpha.
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(context, player_color,
				new OnAmbilWarnaListener() {
					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {
						player_color = color;
						btn_color.setBackgroundColor(color);
					}

					@Override
					public void onCancel(AmbilWarnaDialog dialog) {
						// cancel was selected by the user
					}
				});

		dialog.show();
	}
}
