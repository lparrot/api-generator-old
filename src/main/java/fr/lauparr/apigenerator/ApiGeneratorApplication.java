package fr.lauparr.apigenerator;

import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.entities.ContentField;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import fr.lauparr.apigenerator.repositories.ContentRepository;
import fr.lauparr.apigenerator.services.ContentService;
import fr.lauparr.apigenerator.services.JdbcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.support.Repositories;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class ApiGeneratorApplication implements CommandLineRunner {

	@Autowired
	private ContentRepository contentRepository;
	@Autowired
	private JdbcService tableCreator;
	@Autowired
	private ContentService contentService;

	public static void main(String[] args) {
		SpringApplication.run(ApiGeneratorApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (contentRepository.count() < 1) {
			Content contentUser = Content.builder().name("User").build();
			Content contentBlog = Content.builder().name("Blog").build();
			Content contentComment = Content.builder().name("Comment").build();

			contentUser.addField(ContentField.builder().name("Id").primaryKey(true).contentType(EnumContentFieldType.STRING).hideInList(true).build());
			contentUser.addField(ContentField.builder().name("Username").contentType(EnumContentFieldType.STRING).build());
			contentUser.addField(ContentField.builder().name("Lastname").contentType(EnumContentFieldType.STRING).build());
			contentUser.addField(ContentField.builder().name("Firstname").contentType(EnumContentFieldType.STRING).build());
			contentUser.addField(ContentField.builder().name("Birthday").nullable(true).contentType(EnumContentFieldType.DATE).build());

			contentBlog.addField(ContentField.builder().name("Id").primaryKey(true).contentType(EnumContentFieldType.STRING).hideInList(true).build());
			contentBlog.addField(ContentField.builder().name("Name").contentType(EnumContentFieldType.STRING).build());
			contentBlog.addField(ContentField.builder().name("Description").nullable(true).contentType(EnumContentFieldType.TEXT).build());
			contentBlog.addField(ContentField.builder().name("Content").contentType(EnumContentFieldType.RICHTEXT).build());
			contentBlog.addField(ContentField.builder().name("Unique identifier").nullable(true).contentType(EnumContentFieldType.UID).build());
			contentBlog.addField(ContentField.builder().name("Published Date").nullable(true).contentType(EnumContentFieldType.DATETIME).build());
			contentBlog.addField(ContentField.builder().name("Author").contentType(EnumContentFieldType.RELATION).build());

			contentComment.addField(ContentField.builder().name("Id").primaryKey(true).contentType(EnumContentFieldType.STRING).hideInList(true).build());
			contentComment.addField(ContentField.builder().name("Content").contentType(EnumContentFieldType.TEXT).build());
			contentComment.addField(ContentField.builder().name("Created Date").contentType(EnumContentFieldType.DATETIME).build());
			contentComment.addField(ContentField.builder().name("Author").contentType(EnumContentFieldType.RELATION).build());
			contentComment.addField(ContentField.builder().name("Blog").contentType(EnumContentFieldType.RELATION).build());

//			contentRepository.saveAll(Arrays.asList(contentUser, contentBlog, contentComment));

			contentService.createContent(contentUser);
			contentService.createContent(contentBlog);
			contentService.createContent(contentComment);
		}
		for (Content content : contentRepository.findAll()) {
			tableCreator.create(content);
		}
	}


	@Bean
	public Repositories repositories(ApplicationContext context) {
		return new Repositories(context);
	}

}
