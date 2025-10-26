package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AssertjLibraryAccessors laccForAssertjLibraryAccessors = new AssertjLibraryAccessors(owner);
    private final GrpcLibraryAccessors laccForGrpcLibraryAccessors = new GrpcLibraryAccessors(owner);
    private final JacksonLibraryAccessors laccForJacksonLibraryAccessors = new JacksonLibraryAccessors(owner);
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final KafkaLibraryAccessors laccForKafkaLibraryAccessors = new KafkaLibraryAccessors(owner);
    private final LogbackLibraryAccessors laccForLogbackLibraryAccessors = new LogbackLibraryAccessors(owner);
    private final MicrometerLibraryAccessors laccForMicrometerLibraryAccessors = new MicrometerLibraryAccessors(owner);
    private final MicronautLibraryAccessors laccForMicronautLibraryAccessors = new MicronautLibraryAccessors(owner);
    private final MockitoLibraryAccessors laccForMockitoLibraryAccessors = new MockitoLibraryAccessors(owner);
    private final NettyLibraryAccessors laccForNettyLibraryAccessors = new NettyLibraryAccessors(owner);
    private final PrometheusLibraryAccessors laccForPrometheusLibraryAccessors = new PrometheusLibraryAccessors(owner);
    private final ProtobufLibraryAccessors laccForProtobufLibraryAccessors = new ProtobufLibraryAccessors(owner);
    private final Slf4jLibraryAccessors laccForSlf4jLibraryAccessors = new Slf4jLibraryAccessors(owner);
    private final SpringLibraryAccessors laccForSpringLibraryAccessors = new SpringLibraryAccessors(owner);
    private final TestcontainersLibraryAccessors laccForTestcontainersLibraryAccessors = new TestcontainersLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for hikari (com.zaxxer:HikariCP)
     * with versionRef 'hikari'.
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHikari() {
            return create("hikari");
    }

        /**
         * Creates a dependency provider for postgresql (org.postgresql:postgresql)
     * with versionRef 'postgresql'.
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPostgresql() {
            return create("postgresql");
    }

    /**
     * Returns the group of libraries at assertj
     */
    public AssertjLibraryAccessors getAssertj() {
        return laccForAssertjLibraryAccessors;
    }

    /**
     * Returns the group of libraries at grpc
     */
    public GrpcLibraryAccessors getGrpc() {
        return laccForGrpcLibraryAccessors;
    }

    /**
     * Returns the group of libraries at jackson
     */
    public JacksonLibraryAccessors getJackson() {
        return laccForJacksonLibraryAccessors;
    }

    /**
     * Returns the group of libraries at junit
     */
    public JunitLibraryAccessors getJunit() {
        return laccForJunitLibraryAccessors;
    }

    /**
     * Returns the group of libraries at kafka
     */
    public KafkaLibraryAccessors getKafka() {
        return laccForKafkaLibraryAccessors;
    }

    /**
     * Returns the group of libraries at logback
     */
    public LogbackLibraryAccessors getLogback() {
        return laccForLogbackLibraryAccessors;
    }

    /**
     * Returns the group of libraries at micrometer
     */
    public MicrometerLibraryAccessors getMicrometer() {
        return laccForMicrometerLibraryAccessors;
    }

    /**
     * Returns the group of libraries at micronaut
     */
    public MicronautLibraryAccessors getMicronaut() {
        return laccForMicronautLibraryAccessors;
    }

    /**
     * Returns the group of libraries at mockito
     */
    public MockitoLibraryAccessors getMockito() {
        return laccForMockitoLibraryAccessors;
    }

    /**
     * Returns the group of libraries at netty
     */
    public NettyLibraryAccessors getNetty() {
        return laccForNettyLibraryAccessors;
    }

    /**
     * Returns the group of libraries at prometheus
     */
    public PrometheusLibraryAccessors getPrometheus() {
        return laccForPrometheusLibraryAccessors;
    }

    /**
     * Returns the group of libraries at protobuf
     */
    public ProtobufLibraryAccessors getProtobuf() {
        return laccForProtobufLibraryAccessors;
    }

    /**
     * Returns the group of libraries at slf4j
     */
    public Slf4jLibraryAccessors getSlf4j() {
        return laccForSlf4jLibraryAccessors;
    }

    /**
     * Returns the group of libraries at spring
     */
    public SpringLibraryAccessors getSpring() {
        return laccForSpringLibraryAccessors;
    }

    /**
     * Returns the group of libraries at testcontainers
     */
    public TestcontainersLibraryAccessors getTestcontainers() {
        return laccForTestcontainersLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AssertjLibraryAccessors extends SubDependencyFactory {

        public AssertjLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (org.assertj:assertj-core)
         * with versionRef 'assertj'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("assertj.core");
        }

    }

    public static class GrpcLibraryAccessors extends SubDependencyFactory {

        public GrpcLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for netty (io.grpc:grpc-netty)
         * with versionRef 'grpc'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getNetty() {
                return create("grpc.netty");
        }

            /**
             * Creates a dependency provider for protobuf (io.grpc:grpc-protobuf)
         * with versionRef 'grpc'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getProtobuf() {
                return create("grpc.protobuf");
        }

            /**
             * Creates a dependency provider for stub (io.grpc:grpc-stub)
         * with versionRef 'grpc'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getStub() {
                return create("grpc.stub");
        }

    }

    public static class JacksonLibraryAccessors extends SubDependencyFactory {

        public JacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.fasterxml.jackson.core:jackson-core)
         * with versionRef 'jackson'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("jackson.core");
        }

            /**
             * Creates a dependency provider for databind (com.fasterxml.jackson.core:jackson-databind)
         * with versionRef 'jackson'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getDatabind() {
                return create("jackson.databind");
        }

    }

    public static class JunitLibraryAccessors extends SubDependencyFactory {
        private final JunitJupiterLibraryAccessors laccForJunitJupiterLibraryAccessors = new JunitJupiterLibraryAccessors(owner);

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at junit.jupiter
         */
        public JunitJupiterLibraryAccessors getJupiter() {
            return laccForJunitJupiterLibraryAccessors;
        }

    }

    public static class JunitJupiterLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public JunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jupiter (org.junit.jupiter:junit-jupiter)
         * with versionRef 'junit'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() {
                return create("junit.jupiter");
        }

            /**
             * Creates a dependency provider for api (org.junit.jupiter:junit-jupiter-api)
         * with versionRef 'junit'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("junit.jupiter.api");
        }

            /**
             * Creates a dependency provider for engine (org.junit.jupiter:junit-jupiter-engine)
         * with versionRef 'junit'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getEngine() {
                return create("junit.jupiter.engine");
        }

    }

    public static class KafkaLibraryAccessors extends SubDependencyFactory {

        public KafkaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for clients (org.apache.kafka:kafka-clients)
         * with versionRef 'kafka'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getClients() {
                return create("kafka.clients");
        }

            /**
             * Creates a dependency provider for streams (org.apache.kafka:kafka-streams)
         * with versionRef 'kafka.streams'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getStreams() {
                return create("kafka.streams");
        }

    }

    public static class LogbackLibraryAccessors extends SubDependencyFactory {

        public LogbackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for classic (ch.qos.logback:logback-classic)
         * with versionRef 'logback'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getClassic() {
                return create("logback.classic");
        }

    }

    public static class MicrometerLibraryAccessors extends SubDependencyFactory {
        private final MicrometerRegistryLibraryAccessors laccForMicrometerRegistryLibraryAccessors = new MicrometerRegistryLibraryAccessors(owner);

        public MicrometerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (io.micrometer:micrometer-core)
         * with versionRef 'micrometer'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("micrometer.core");
        }

        /**
         * Returns the group of libraries at micrometer.registry
         */
        public MicrometerRegistryLibraryAccessors getRegistry() {
            return laccForMicrometerRegistryLibraryAccessors;
        }

    }

    public static class MicrometerRegistryLibraryAccessors extends SubDependencyFactory {

        public MicrometerRegistryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for prometheus (io.micrometer:micrometer-registry-prometheus)
         * with versionRef 'micrometer'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPrometheus() {
                return create("micrometer.registry.prometheus");
        }

    }

    public static class MicronautLibraryAccessors extends SubDependencyFactory {

        public MicronautLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for http (io.micronaut:micronaut-http)
         * with versionRef 'micronaut'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getHttp() {
                return create("micronaut.http");
        }

            /**
             * Creates a dependency provider for inject (io.micronaut:micronaut-inject)
         * with versionRef 'micronaut'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getInject() {
                return create("micronaut.inject");
        }

            /**
             * Creates a dependency provider for validation (io.micronaut:micronaut-validation)
         * with versionRef 'micronaut'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getValidation() {
                return create("micronaut.validation");
        }

    }

    public static class MockitoLibraryAccessors extends SubDependencyFactory {
        private final MockitoJunitLibraryAccessors laccForMockitoJunitLibraryAccessors = new MockitoJunitLibraryAccessors(owner);

        public MockitoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at mockito.junit
         */
        public MockitoJunitLibraryAccessors getJunit() {
            return laccForMockitoJunitLibraryAccessors;
        }

    }

    public static class MockitoJunitLibraryAccessors extends SubDependencyFactory {

        public MockitoJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jupiter (org.mockito:mockito-junit-jupiter)
         * with versionRef 'mockito'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJupiter() {
                return create("mockito.junit.jupiter");
        }

    }

    public static class NettyLibraryAccessors extends SubDependencyFactory {

        public NettyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for all (io.netty:netty-all)
         * with versionRef 'netty'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAll() {
                return create("netty.all");
        }

    }

    public static class PrometheusLibraryAccessors extends SubDependencyFactory {

        public PrometheusLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for metrics (io.prometheus:prometheus-metrics-core)
         * with versionRef 'prometheus'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMetrics() {
                return create("prometheus.metrics");
        }

    }

    public static class ProtobufLibraryAccessors extends SubDependencyFactory {

        public ProtobufLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for java (com.google.protobuf:protobuf-java)
         * with versionRef 'protobuf'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJava() {
                return create("protobuf.java");
        }

    }

    public static class Slf4jLibraryAccessors extends SubDependencyFactory {

        public Slf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.slf4j:slf4j-api)
         * with versionRef 'slf4j'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("slf4j.api");
        }

    }

    public static class SpringLibraryAccessors extends SubDependencyFactory {
        private final SpringBootLibraryAccessors laccForSpringBootLibraryAccessors = new SpringBootLibraryAccessors(owner);

        public SpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at spring.boot
         */
        public SpringBootLibraryAccessors getBoot() {
            return laccForSpringBootLibraryAccessors;
        }

    }

    public static class SpringBootLibraryAccessors extends SubDependencyFactory {
        private final SpringBootConfigurationLibraryAccessors laccForSpringBootConfigurationLibraryAccessors = new SpringBootConfigurationLibraryAccessors(owner);
        private final SpringBootStarterLibraryAccessors laccForSpringBootStarterLibraryAccessors = new SpringBootStarterLibraryAccessors(owner);

        public SpringBootLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at spring.boot.configuration
         */
        public SpringBootConfigurationLibraryAccessors getConfiguration() {
            return laccForSpringBootConfigurationLibraryAccessors;
        }

        /**
         * Returns the group of libraries at spring.boot.starter
         */
        public SpringBootStarterLibraryAccessors getStarter() {
            return laccForSpringBootStarterLibraryAccessors;
        }

    }

    public static class SpringBootConfigurationLibraryAccessors extends SubDependencyFactory {

        public SpringBootConfigurationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for processor (org.springframework.boot:spring-boot-configuration-processor)
         * with versionRef 'spring.boot'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getProcessor() {
                return create("spring.boot.configuration.processor");
        }

    }

    public static class SpringBootStarterLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public SpringBootStarterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for starter (org.springframework.boot:spring-boot-starter)
         * with versionRef 'spring.boot'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() {
                return create("spring.boot.starter");
        }

            /**
             * Creates a dependency provider for actuator (org.springframework.boot:spring-boot-starter-actuator)
         * with versionRef 'spring.boot'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getActuator() {
                return create("spring.boot.starter.actuator");
        }

            /**
             * Creates a dependency provider for web (org.springframework.boot:spring-boot-starter-web)
         * with versionRef 'spring.boot'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getWeb() {
                return create("spring.boot.starter.web");
        }

    }

    public static class TestcontainersLibraryAccessors extends SubDependencyFactory {

        public TestcontainersLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit (org.testcontainers:junit-jupiter)
         * with versionRef 'testcontainers'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJunit() {
                return create("testcontainers.junit");
        }

            /**
             * Creates a dependency provider for kafka (org.testcontainers:kafka)
         * with versionRef 'testcontainers'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKafka() {
                return create("testcontainers.kafka");
        }

            /**
             * Creates a dependency provider for postgresql (org.testcontainers:postgresql)
         * with versionRef 'testcontainers'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPostgresql() {
                return create("testcontainers.postgresql");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final KafkaVersionAccessors vaccForKafkaVersionAccessors = new KafkaVersionAccessors(providers, config);
        private final SpringVersionAccessors vaccForSpringVersionAccessors = new SpringVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: assertj (3.24.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAssertj() { return getVersion("assertj"); }

            /**
             * Returns the version associated to this alias: gradle (8.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGradle() { return getVersion("gradle"); }

            /**
             * Returns the version associated to this alias: grpc (1.59.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGrpc() { return getVersion("grpc"); }

            /**
             * Returns the version associated to this alias: hikari (5.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHikari() { return getVersion("hikari"); }

            /**
             * Returns the version associated to this alias: jackson (2.15.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJackson() { return getVersion("jackson"); }

            /**
             * Returns the version associated to this alias: java (17)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJava() { return getVersion("java"); }

            /**
             * Returns the version associated to this alias: junit (5.10.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJunit() { return getVersion("junit"); }

            /**
             * Returns the version associated to this alias: logback (1.4.11)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLogback() { return getVersion("logback"); }

            /**
             * Returns the version associated to this alias: micrometer (1.11.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMicrometer() { return getVersion("micrometer"); }

            /**
             * Returns the version associated to this alias: micronaut (4.2.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMicronaut() { return getVersion("micronaut"); }

            /**
             * Returns the version associated to this alias: mockito (5.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMockito() { return getVersion("mockito"); }

            /**
             * Returns the version associated to this alias: netty (4.1.100.Final)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getNetty() { return getVersion("netty"); }

            /**
             * Returns the version associated to this alias: postgresql (42.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPostgresql() { return getVersion("postgresql"); }

            /**
             * Returns the version associated to this alias: prometheus (0.16.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPrometheus() { return getVersion("prometheus"); }

            /**
             * Returns the version associated to this alias: protobuf (3.25.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getProtobuf() { return getVersion("protobuf"); }

            /**
             * Returns the version associated to this alias: slf4j (2.0.9)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSlf4j() { return getVersion("slf4j"); }

            /**
             * Returns the version associated to this alias: testcontainers (1.19.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTestcontainers() { return getVersion("testcontainers"); }

            /**
             * Returns the version associated to this alias: timescaledb (2.13.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTimescaledb() { return getVersion("timescaledb"); }

        /**
         * Returns the group of versions at versions.kafka
         */
        public KafkaVersionAccessors getKafka() {
            return vaccForKafkaVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.spring
         */
        public SpringVersionAccessors getSpring() {
            return vaccForSpringVersionAccessors;
        }

    }

    public static class KafkaVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public KafkaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the version associated to this alias: kafka (3.6.0)
         * If the version is a rich version and that its not expressible as a
         * single version string, then an empty string is returned.
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("kafka"); }

            /**
             * Returns the version associated to this alias: kafka.streams (3.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getStreams() { return getVersion("kafka.streams"); }

    }

    public static class SpringVersionAccessors extends VersionFactory  {

        public SpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: spring.boot (3.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBoot() { return getVersion("spring.boot"); }

    }

    public static class BundleAccessors extends BundleFactory {
        private final SpringBundleAccessors baccForSpringBundleAccessors = new SpringBundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

            /**
             * Creates a dependency bundle provider for grpc which is an aggregate for the following dependencies:
             * <ul>
             *    <li>io.grpc:grpc-protobuf</li>
             *    <li>io.grpc:grpc-stub</li>
             *    <li>io.grpc:grpc-netty</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getGrpc() {
                return createBundle("grpc");
            }

            /**
             * Creates a dependency bundle provider for kafka which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.apache.kafka:kafka-clients</li>
             *    <li>org.apache.kafka:kafka-streams</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getKafka() {
                return createBundle("kafka");
            }

            /**
             * Creates a dependency bundle provider for logging which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.slf4j:slf4j-api</li>
             *    <li>ch.qos.logback:logback-classic</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getLogging() {
                return createBundle("logging");
            }

            /**
             * Creates a dependency bundle provider for metrics which is an aggregate for the following dependencies:
             * <ul>
             *    <li>io.micrometer:micrometer-core</li>
             *    <li>io.micrometer:micrometer-registry-prometheus</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getMetrics() {
                return createBundle("metrics");
            }

            /**
             * Creates a dependency bundle provider for testing which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.junit.jupiter:junit-jupiter</li>
             *    <li>org.mockito:mockito-junit-jupiter</li>
             *    <li>org.assertj:assertj-core</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getTesting() {
                return createBundle("testing");
            }

        /**
         * Returns the group of bundles at bundles.spring
         */
        public SpringBundleAccessors getSpring() {
            return baccForSpringBundleAccessors;
        }

    }

    public static class SpringBundleAccessors extends BundleFactory {
        private final SpringBootBundleAccessors baccForSpringBootBundleAccessors = new SpringBootBundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);

        public SpringBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Returns the group of bundles at bundles.spring.boot
         */
        public SpringBootBundleAccessors getBoot() {
            return baccForSpringBootBundleAccessors;
        }

    }

    public static class SpringBootBundleAccessors extends BundleFactory {

        public SpringBootBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

            /**
             * Creates a dependency bundle provider for spring.boot.starter which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.springframework.boot:spring-boot-starter</li>
             *    <li>org.springframework.boot:spring-boot-starter-web</li>
             *    <li>org.springframework.boot:spring-boot-starter-actuator</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getStarter() {
                return createBundle("spring.boot.starter");
            }

    }

    public static class PluginAccessors extends PluginFactory {
        private final MicronautPluginAccessors paccForMicronautPluginAccessors = new MicronautPluginAccessors(providers, config);
        private final SpringPluginAccessors paccForSpringPluginAccessors = new SpringPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for grpc to the plugin id 'com.google.protobuf'
             * with versionRef 'protobuf'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getGrpc() { return createPlugin("grpc"); }

        /**
         * Returns the group of plugins at plugins.micronaut
         */
        public MicronautPluginAccessors getMicronaut() {
            return paccForMicronautPluginAccessors;
        }

        /**
         * Returns the group of plugins at plugins.spring
         */
        public SpringPluginAccessors getSpring() {
            return paccForSpringPluginAccessors;
        }

    }

    public static class MicronautPluginAccessors extends PluginFactory {

        public MicronautPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for micronaut.application to the plugin id 'io.micronaut.application'
             * with versionRef 'micronaut'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getApplication() { return createPlugin("micronaut.application"); }

    }

    public static class SpringPluginAccessors extends PluginFactory {
        private final SpringDependencyPluginAccessors paccForSpringDependencyPluginAccessors = new SpringDependencyPluginAccessors(providers, config);

        public SpringPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for spring.boot to the plugin id 'org.springframework.boot'
             * with versionRef 'spring.boot'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getBoot() { return createPlugin("spring.boot"); }

        /**
         * Returns the group of plugins at plugins.spring.dependency
         */
        public SpringDependencyPluginAccessors getDependency() {
            return paccForSpringDependencyPluginAccessors;
        }

    }

    public static class SpringDependencyPluginAccessors extends PluginFactory {

        public SpringDependencyPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for spring.dependency.management to the plugin id 'io.spring.dependency-management'
             * with version '1.1.4'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getManagement() { return createPlugin("spring.dependency.management"); }

    }

}
