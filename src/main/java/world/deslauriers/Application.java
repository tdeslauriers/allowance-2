package world.deslauriers;

import io.dekorate.docker.annotation.DockerBuild;
import io.dekorate.kubernetes.annotation.*;
import io.micronaut.runtime.Micronaut;

@KubernetesApplication(
    name = "allowance",
    labels = @Label(key = "app", value = "allowance"),
    serviceType = ServiceType.ClusterIP,
    replicas = 1, // until install schedlock so daily tasks aren't duplicative.
    imagePullPolicy = ImagePullPolicy.Always,
    ports = @Port(name = "http", hostPort = 8080, containerPort = 8083),
    livenessProbe = @Probe(httpActionPath = "/health/liveness", initialDelaySeconds = 5, timeoutSeconds = 3, failureThreshold = 10),
    readinessProbe = @Probe(httpActionPath = "/health/readiness", initialDelaySeconds = 5, timeoutSeconds = 3, failureThreshold = 10),
    envVars = {
        @Env(name = "ALLOWANCE_R2DBC_URL", configmap = "allowance-svc-config", value = "r2dbc_url"),
        @Env(name = "ALLOWANCE_JDBC_URL", configmap = "allowance-svc-config", value = "jdbc_url"),
        @Env(name = "ALLOWANCE_JDBC_USER", configmap = "allowance-svc-config", value = "jdbc_username"),
        @Env(name = "ALLOWANCE_LOGIN_URL", configmap = "allowance-svc-config", value = "login_url"),
        @Env(name = "ALLOWANCE_LOGIN_USERNAME", configmap = "allowance-svc-config", value = "login_username"),
        @Env(name = "ALLOWANCE_JDBC_PASSWORD", secret = "allowance-mariadb", value = "mariadb-password"),
        @Env(name = "ALLOWANCE_LOGIN_PASSWORD", secret = "allowance-service-password", value = "login-password"),
        @Env(name = "JWT_GENERATOR_SIGNATURE_SECRET", secret = "jwt", value = "signature-pw"),
    }
)
@DockerBuild(group = "tdeslauriers", name = "allowance")
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
