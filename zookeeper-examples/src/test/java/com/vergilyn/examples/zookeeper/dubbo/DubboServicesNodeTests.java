package com.vergilyn.examples.zookeeper.dubbo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.client.ServiceInstance;
import org.apache.dubbo.registry.zookeeper.ZookeeperInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 *
 * @author vergilyn
 * @since 2022-11-01
 *
 * @see org.apache.dubbo.registry.zookeeper.ZookeeperServiceDiscovery#doRegister(ServiceInstance)
 */
@SuppressWarnings("JavadocReference")
public class DubboServicesNodeTests {
	private static final String ZK_ADDRESS = "host.docker.internal:2181";

	@Test
	public void create(){
		URL mockDubboURL = Mockito.mock(URL.class);

		// org.apache.dubbo.registry.zookeeper.util.CuratorFrameworkUtils.buildCuratorFramework
		Mockito.when(mockDubboURL.getBackupAddress()).thenReturn("");

		org.apache.curator.x.discovery.ServiceDiscovery<ZookeeperInstance> serviceDiscovery;

	}

	public static ServiceDiscovery<ZookeeperInstance> buildServiceDiscovery(CuratorFramework curatorFramework,
			String basePath) {

		return ServiceDiscoveryBuilder.builder(ZookeeperInstance.class)
				.client(curatorFramework)
				.basePath(basePath)
				.build();
	}
}
