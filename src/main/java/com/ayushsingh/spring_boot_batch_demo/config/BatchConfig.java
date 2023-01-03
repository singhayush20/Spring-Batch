package com.ayushsingh.spring_boot_batch_demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing //this is important- enables batch processing in the project
public class BatchConfig {
    @Autowired
    private DataSource dataSource;

    
    // @Autowired
    // PlatformTransactionManager platformTransactionManager;
    // @Autowired JobRepository jobRepository;
    @Bean
    public FlatFileItemReader<User> reader(){
        FlatFileItemReader<User> reader=new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("records.csv"));
        reader.setLineMapper(getLineMapper()); //how to map the line which is read.
        reader.setLinesToSkip(1); // skip lines when error occurs
        return reader;
    }
    private LineMapper<User> getLineMapper() {
        DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();

        delimitedLineTokenizer.setNames(new String[]{"Roll Number","Name of Student","Personal Email ID","SMVDU Email ID","MOBILE NO"});
        delimitedLineTokenizer.setIncludedFields(new int[]{1,2,3,4,5});

        BeanWrapperFieldSetMapper<User> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
    @Bean
    public UserItemProcessor processor(){
        return new UserItemProcessor();
    }
    @Bean
    public JdbcBatchItemWriter<User> writer(){
        JdbcBatchItemWriter<User> writer= new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
        writer.setSql("insert into user(rollNo,name,personalEmail,officialEmail,mobileNo) values (:rollNo,:name,:personalEmail,:officalEmail,:mobileNo)");
        writer.setDataSource(dataSource);
        return writer;
    }


    // @Bean
    // public Job importUserJob(){
    //     return this.jobBuilderFactory.get("USER-IMPORT-JOB")
    //     .incrementer(new RunIdIncrementer())
    //     .flow(step1())
    //     .end()
    //     .build();
    // }

    @Bean 
    public Job importUserJob(){

        return new JobBuilder("USER_IMPORT_JOB")
        .incrementer(new RunIdIncrementer())
        .flow(step1())
        .end()
        .build();
    }
    
    // @Bean
    // public Step step1() {
    //    return this.stepBuilderFactory.get("Step 1")
    //    .<User,User>chunk(5)
    //    .reader(reader())
    //    .processor(processor())
    //    .writer(writer())
    //    .build();

       
    // }
    @Bean
    public Step step1(){
        return new StepBuilder("Step 1")
        .<User,User>chunk(5)
        .reader(reader())
        .writer(writer())
        .build();
    }
}
