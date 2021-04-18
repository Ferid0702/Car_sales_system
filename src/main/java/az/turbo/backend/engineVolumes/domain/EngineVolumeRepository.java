package az.turbo.backend.engineVolumes.domain;

import az.turbo.backend.engineVolumes.domain.model.EngineVolume;
import az.turbo.backend.shared.PostgreDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EngineVolumeRepository {
    private PostgreDbService postgreDbService;

    @Autowired
    public EngineVolumeRepository(PostgreDbService postgreDbService) {
        this.postgreDbService = postgreDbService;
    }

    public List<EngineVolume> findAll() {
        try {
            List<EngineVolume> engineVolumes = new ArrayList<>();
            Connection connection = postgreDbService.getConnection();
            String query = "select id,name from enginevolumes";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                engineVolumes.add(new EngineVolume(id, name));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
            return engineVolumes;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<EngineVolume> findById(long id) {
        try {
            Optional<EngineVolume> optionalEngineVolume = Optional.empty();

            Connection connection = postgreDbService.getConnection();
            String query = "SELECT * FROM engine-volumes WHERE id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                EngineVolume engineVolume = new EngineVolume(id, name);
                optionalEngineVolume = Optional.of(engineVolume);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return optionalEngineVolume;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public long create(EngineVolume engineVolume) {
        try {
            Connection connection = postgreDbService.getConnection();
            String query = "insert into enginevolumes(name,created_by,created_date)" +
                    "values(?,?,?) returning id";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, engineVolume.getValue());
            preparedStatement.setLong(2, engineVolume.getUpdatedBy());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(engineVolume.getUpdatedDate()));

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long id = resultSet.getLong(1);
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return id;


        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(EngineVolume engineVolume) {
        try {
            Connection connection = postgreDbService.getConnection();

            String query = "Update engine-volumes" +
                    "SET name=?,updated_by=?,updated_date=?" +
                    "where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, engineVolume.getValue());
            preparedStatement.setLong(2, engineVolume.getUpdatedBy());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(engineVolume.getUpdatedDate()));
            preparedStatement.setLong(4, engineVolume.getId());

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
