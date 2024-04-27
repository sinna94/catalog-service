# 빌드
custom_build(
    ref = 'catalog-service',
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    deps = ['build.gradle.kts', 'settings.gradle.kts', 'src']
)

# 배포
k8s_yaml(['polar-deployment/kubernetes/deployment.yml', 'polar-deployment/kubernetes/service.yml'])

# 관리
k8s_resource('catalog-service', port_forwards=['8080'])