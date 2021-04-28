package az.turbo.backend.brands.domain;

import az.turbo.backend.brands.domain.model.Brand;
import az.turbo.backend.shared.PostgreDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class BrandRepository {
    private PostgreDbService postgreDbService;

    @Autowired
    public BrandRepository(PostgreDbService postgreDbService) {
        this.postgreDbService = postgreDbService;
    }


    public long create(Brand brand) {
        try {
            Connection connection = postgreDbService.getConnection();
            String query = "insert into brands(name,created_by,created_date)" +
                    "values(?,?,?) returning id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, brand.getName());
            preparedStatement.setLong(2, brand.getCreatedBy());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(brand.getCreatedDate()));
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
}
