package com.vijaya.caster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.vijaya.caster.dao.mappers.ProfileRowMapper;
import com.vijaya.caster.domain.Profile;

@Repository(value = "profileDao")
public class ProfileDaoImpl implements ProfileDao {

	private static final Logger logger = LoggerFactory.getLogger(ProfileDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long saveProfile(Profile profile) {

		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

		String sql = "insert into profiles (first_name, last_name, date_Of_Birth, gender, email, PHONE, ALTERNATE_PHONE) "
				+ "values(?,?,?,?,?,?,?)";

		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {

				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, profile.getFirstName());
				ps.setString(2, profile.getLastName());
				if (profile.getDateOfBirth() != null) {
					ps.setDate(3, new java.sql.Date(profile.getDateOfBirth().getTime()));
				} else {
					ps.setNull(3, java.sql.Types.DATE);
				}
				ps.setString(4, profile.getGender());
				ps.setString(5, profile.getEmail());
				ps.setString(6, profile.getPhone());
				ps.setString(7, profile.getAlternatePhone());

				return ps;
			}
		}, generatedKeyHolder);

		logger.info("profile crated with profileId={}", generatedKeyHolder.getKey().longValue());
		return generatedKeyHolder.getKey().longValue();
	}

	@Override
	public Profile getProfileById(Long profileId) {
		logger.info("Inside Dao getProfileById method");
		String sql = "SELECT PROFILE_ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH,"
				+ " GENDER, PHONE, ALTERNATE_PHONE from PROFILES WHERE PROFILE_ID=?";
		Profile p = null;
		try {
			p = this.jdbcTemplate.queryForObject(sql, new Object[] { profileId }, new ProfileRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return p;
	}

	@Override
	public int updateProfile(Profile profile) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteProfile(Long profileId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Profile getProfileByEmail(String emailId) {
		logger.info("Inside Dao getProfileById method");
		String sql = "SELECT PROFILE_ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH,"
				+ " GENDER, PHONE, ALTERNATE_PHONE from PROFILES WHERE EMAIL=?";
		Profile p = null;
		try {
			p = this.jdbcTemplate.queryForObject(sql, new Object[] { emailId }, new ProfileRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return p;
	}

}
