package com.demo.users.config;

import org.apache.commons.lang.CharEncoding;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class VelocityJdbcTemplate extends JdbcTemplate {
    private static final String SUB_DIR = "/sql";

    public VelocityJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    @PostConstruct
    public void init() {
        Properties p = new Properties();
        p.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        p.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init( p );
    }

    public String makeTemplate(String templateName, Map<String, Object> params) {
        PreparedToolContext preparedToolContext = new PreparedToolContext(templateName, params, this);
        return preparedToolContext.getSql();
    }

    public String makeTemplate(String templateName) {
        return makeTemplate(templateName, new HashMap<>());
    }

    private static class PreparedToolContext {
        private String sql;
        private Map<String, ?> params;

        public PreparedToolContext(String templateName, Map<String, ?> params, VelocityJdbcTemplate velocityJdbcTemplate) {
            VelocityContext context = new VelocityContext();
            if(params != null) {
                params.keySet().stream().forEach(k -> {
                    context.put((String) k, params.get(k));
                });
            }
            context.put("_", velocityJdbcTemplate);
            StringWriter writer = new StringWriter();
            Velocity.mergeTemplate(new StringBuilder(SUB_DIR).append(templateName).toString(), CharEncoding.UTF_8, context, writer);
            writer.flush();
            sql = writer.toString();
            this.params = params;
        }

        public String getSql() {
            return sql;
        }

        public Map<String, ?> getParams() {
            return params;
        }
    }
}
