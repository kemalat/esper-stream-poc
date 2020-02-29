package de.oriontec.esper.app.config;


import de.oriontec.esper.app.domain.Authorities;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


  private static final String ENV_OAUTH = "authentication.oauth.";
  private static final String PROP_CLIENT_ID = "clientid";
  private static final String PROP_SECRET = "secret";
  private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";


  @Value("${resource.transactionId:spring-boot-application}")
  private String resourceId;


  @Autowired
  private DataSource dataSource;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Bean
  public TokenStore tokenStore() {
    return new JdbcTokenStore(dataSource);
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    return new JwtAccessTokenConverter();
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.tokenStore(tokenStore()).authenticationManager(this.authenticationManager)
        .accessTokenConverter(accessTokenConverter());
  }


  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
  }


  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory().withClient(PROP_CLIENT_ID).authorizedGrantTypes("password")
//                .authorities("ROLE_TRUSTED_CLIENT")
        .authorities(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_MODERATOR.name(), Authorities.ROLE_REPORTER.name())
        .scopes("read", "write").resourceIds(resourceId)
        .accessTokenValiditySeconds(3600)
        .secret(PROP_SECRET);
  }


}
