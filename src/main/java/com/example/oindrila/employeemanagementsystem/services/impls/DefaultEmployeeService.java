package com.example.oindrila.employeemanagementsystem.services.impls;

import com.example.oindrila.employeemanagementsystem.entities.PhotoInfoEntity;
import com.example.oindrila.employeemanagementsystem.entities.employee.EmployeeEntity;
import com.example.oindrila.employeemanagementsystem.models.ImageResource;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeRegistrationRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeUpdateRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.FilterRequest;
import com.example.oindrila.employeemanagementsystem.models.Employee;
import com.example.oindrila.employeemanagementsystem.repositories.EmployeeRepository;
import com.example.oindrila.employeemanagementsystem.services.EmployeeService;
import com.example.oindrila.employeemanagementsystem.services.ImageService;
import com.example.oindrila.employeemanagementsystem.utilities.ResourceUtility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class DefaultEmployeeService implements EmployeeService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ImageService imageService;

    @PostConstruct
    private void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Employee createEmployee(final EmployeeRegistrationRequest employeeRegistrationRequest) {
        log.info("Received Employee Registration Request: {}", employeeRegistrationRequest);
        final ImageResource imageResource = new ImageResource(
                employeeRegistrationRequest.getImage(),
                ResourceUtility.generateFileName(employeeRegistrationRequest));
        try {
            imageService.uploadImage(imageResource);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setFirstName(employeeRegistrationRequest.getFirstName());
        employeeEntity.setLastName(employeeRegistrationRequest.getLastName());
        employeeEntity.setEmail(employeeRegistrationRequest.getEmail());
        employeeEntity.setSalary(employeeRegistrationRequest.getSalary());
        employeeEntity.setDepartment(employeeRegistrationRequest.getDepartment());
        employeeEntity.setGender(employeeRegistrationRequest.getGender());
        employeeEntity.setPosition(employeeRegistrationRequest.getPosition());
        employeeEntity.setPhone(employeeRegistrationRequest.getPhone());;

        final PhotoInfoEntity photoInfoEntity = new PhotoInfoEntity();
        photoInfoEntity.setName(imageResource.getFileName());
        photoInfoEntity.setEmployeeEntity(employeeEntity);
        employeeEntity.setPhotoInfo(photoInfoEntity);
        employeeEntity = employeeRepository.save(employeeEntity);
        return objectMapper.convertValue(employeeEntity, Employee.class);
    }

    @Override
    public List<Employee> findEmployeesByFilterCriteria(final FilterRequest filterRequest) {
        log.info("Filter Request received  : {} ", filterRequest);
        final EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setCreatedOn(null);
        if (filterRequest.getDepartment() != null) {
            employeeEntity.setDepartment(filterRequest.getDepartment());
        }
        if (StringUtils.hasText(filterRequest.getPosition())) {
            employeeEntity.setPosition(filterRequest.getPosition());
        }
        log.info("Example Entity: {}", employeeEntity);
        final Example<EmployeeEntity> example = Example.of(employeeEntity);
        final Page<EmployeeEntity> employeeEntityPage = employeeRepository.findAll(example, PageRequest.of(0, filterRequest.getPageLimit()));
        log.info("Retrieved Employee Entities: {}", employeeEntityPage);
        final List<EmployeeEntity> retrievedList = employeeEntityPage.getContent();
        final List<Employee> employeeCreatedResponseList = new ArrayList<>();
        retrievedList.forEach((entity) -> {
            final Employee employeeCreatedResponse = objectMapper.convertValue(entity, Employee.class);
            employeeCreatedResponseList.add(employeeCreatedResponse);
        });
        return employeeCreatedResponseList;
    }

    @Override
    @Transactional
    public List<Employee> findAllEmployees() {
        log.info("Fetching all employees");
        final List<EmployeeEntity> employeeEntityList = employeeRepository.findAll();
        final List<Employee> employeeList = new ArrayList<>();
        employeeEntityList.forEach((empEntity) -> {
            final Employee employee = objectMapper.convertValue(empEntity, Employee.class);
            try {
                final URL url = imageService.getImageURLByPhotoInfo(employee.getPhotoInfo());
                employee.getPhotoInfo().setUrl(url.toString());
                employeeList.add(employee);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Retrieved All Employees: {}", employeeList);
        return employeeList;
    }


    @Override
    public Optional<Employee> findById(final Long id) {
        log.info("Getting Employee Details by ID: {}", id);
        final Optional<EmployeeEntity> employeeEntityOptional = employeeRepository.findById(id);
        final AtomicReference<Employee> employeeAtomicReference = new AtomicReference<>(null);
        employeeRepository.findById(id).ifPresent((entity) -> {
            final Employee e = objectMapper.convertValue(entity, Employee.class);
            try {
                final URL url = imageService.getImageURLByPhotoInfo(e.getPhotoInfo());
                e.getPhotoInfo().setUrl(url.toString());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            employeeAtomicReference.set(e);
        });
        return Optional.ofNullable(employeeAtomicReference.get());
    }

    @Override
    @Transactional
    public Optional<Employee> updateEmployeeById(final Long id, final EmployeeUpdateRequest employeeUpdateRequest) {
        log.info("Updating Employee Details of ID: {} to {}", id, employeeUpdateRequest);
        final Optional<EmployeeEntity> employeeEntityOptional = employeeRepository.findById(id);
        if (employeeEntityOptional.isEmpty()) {
            return Optional.empty();
        }
        EmployeeEntity employeeEntity = employeeEntityOptional.get();
        if (StringUtils.hasText(employeeUpdateRequest.getFirstName())) {
            employeeEntity.setFirstName(employeeUpdateRequest.getFirstName());
        }
        if (StringUtils.hasText(employeeUpdateRequest.getLastName())) {
            employeeEntity.setLastName(employeeUpdateRequest.getLastName());
        }
        if (employeeUpdateRequest.getSalary() != null) {
            employeeEntity.setSalary(employeeUpdateRequest.getSalary());
        }
        if (StringUtils.hasText(employeeUpdateRequest.getPosition())) {
            employeeEntity.setPosition(employeeUpdateRequest.getPosition());
        }
        if (employeeUpdateRequest.getDepartment() != null) {
            employeeEntity.setDepartment(employeeUpdateRequest.getDepartment());
        }
        if (employeeUpdateRequest.getGender() != null) {
            employeeEntity.setGender(employeeUpdateRequest.getGender());
        }
        if (StringUtils.hasText(employeeUpdateRequest.getEmail())) {
            employeeEntity.setEmail(employeeUpdateRequest.getEmail());
        }
        log.info("Updated Employee Entity: {}", employeeEntity);
        return Optional.of(objectMapper.convertValue(employeeEntity, Employee.class));
    }

    @Override
    public Boolean deleteById(final Long id) {
        log.info("Deleting Employee by ID: {}", id);
        employeeRepository.deleteById(id);
        return Boolean.TRUE;
    }


}
