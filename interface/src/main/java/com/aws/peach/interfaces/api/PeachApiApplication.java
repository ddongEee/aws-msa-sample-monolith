package com.aws.peach.interfaces.api;

import com.aws.peach.configuration.PeachApiContextConfig;
import com.aws.peach.infrastructure.configuration.api.PeachApiInfrastructureContextConfig;
import com.aws.peach.domain.inventory.entity.Inventory;
import com.aws.peach.domain.inventory.repository.InventoryRepository;
import com.aws.peach.domain.member.Member;
import com.aws.peach.domain.member.MemberRepository;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import com.aws.peach.domain.order.vo.OrderState;
import com.aws.peach.domain.order.vo.Orderer;
import com.aws.peach.domain.product.repository.ProductRepository;
import com.aws.peach.infrastructure.configuration.DummyConfiguration;
import com.aws.peach.infrastructure.configuration.KafkaMessageConfiguration;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.DummyShippingInformation.EUNJU_SHIPPING_INFORMATION;
import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.DummyShippingInformation.HAKSUNG_SHIPPING_INFORMATION;
import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.ProductType.GOLD_PEACH;
import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.ProductType.PEACH;


@Slf4j
@SpringBootApplication
@Import(value = {PeachApiContextConfig.class})
public class PeachApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeachApiApplication.class, args);
	}

	// todo : 아래 db 초기화 프로세스는 flyway 로 전환시 사라질 예정임
	@Slf4j
	@Component
	@RequiredArgsConstructor
	public static class StartupInitializer {
		private final MemberRepository memberRepository;
		private final InventoryRepository inventoryRepository;
		private final OrderRepository orderRepository;
		private final ProductRepository productRepository;

		@EventListener(ContextRefreshedEvent.class)
		public void contextRefreshedEvent() {
			initMemberDb();
			initInventoryDb();
			initOrderDb();
			initProductDb();
			callSample();
		}

		private void callSample() {
			List<Order> byOrderNoIn = orderRepository.findByOrderNumberIn(Arrays.asList(OrderNumber.create("1"), OrderNumber.create("2")));
			log.info("@@@@@@@@@@@@@@ : {}", byOrderNoIn);
		}


		private void initMemberDb() {
			Member heejong = memberRepository.findByMemberId("isheejong");
			if (heejong == null) {
				heejong = memberRepository.save(Member.builder()
						.memberId("isheejong")
						.memberName("이희종")
						.build());
			}
			log.info("@@@ Hello leehee : {}", heejong.getMemberName());
		}

		private void initInventoryDb() {
			Inventory samplePeach = inventoryRepository.findByProductIdAndDate("SamplePeach", LocalDate.of(2022,1,1));
			if (samplePeach == null) {
				inventoryRepository.save(
						Inventory.builder()
								.productId("SamplePeach")
								.date(LocalDate.of(2022,1,1))
								.count(12)
								.build()
				);
				List<Inventory> inventoryList = Lists.newArrayList(
						Inventory.builder()
								.productId("GOOD-PEACH-1")
								.date(LocalDate.of(2021,12,13))
								.count(12)
								.build(),
						Inventory.builder()
								.productId("GOOD-PEACH-1")
								.date(LocalDate.of(2021,12,14))
								.count(20)
								.build(),
						Inventory.builder()
								.productId("GOOD-PEACH-1")
								.date(LocalDate.of(2021,12,15))
								.count(30)
								.build(),
						Inventory.builder()
								.productId("GOOD-PEACH-2")
								.date(LocalDate.of(2021,12,13))
								.count(5)
								.build(),
						Inventory.builder()
								.productId("GOOD-PEACH-2")
								.date(LocalDate.of(2021,12,14))
								.count(8)
								.build(),
						Inventory.builder()
								.productId("GOOD-PEACH-2")
								.date(LocalDate.of(2021,12,15))
								.count(9)
								.build()
				);
				inventoryList.forEach(inventoryRepository::save);
			}
		}

		private void initOrderDb() {
			List<Order> foundOrders = orderRepository.findByOrderNumberIn(Collections.singletonList(
					OrderNumber.builder().orderNumber("1").build()
			));
			if (!foundOrders.isEmpty()) {
				return;
			}

			List<Order> ordersForSave = Arrays.asList(Order.builder()
							.orderNumber(OrderNumber.builder().orderNumber("1").build())
							.orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
							.orderLines(Collections.singletonList(PEACH.makeOrderProduct(10)))
							.shippingInformation(HAKSUNG_SHIPPING_INFORMATION.make())
							.orderState(OrderState.UNPAID) // todo : crayon : 어제 주문이지만 아직 결제가 안된 케이스 커버는?
							.orderDate(LocalDate.now()) // 어제
							.build(),
					Order.builder()
							.orderNumber(OrderNumber.builder().orderNumber("2").build())
							.orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
							.orderLines(Collections.singletonList(PEACH.makeOrderProduct(20)))
							.shippingInformation(HAKSUNG_SHIPPING_INFORMATION.make())
							.orderState(OrderState.UNPAID)
							.orderDate(LocalDate.now()) // 오늘
							.build(),
					Order.builder()
							.orderNumber(OrderNumber.builder().orderNumber("3").build())
							.orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
							.orderLines(Arrays.asList(PEACH.makeOrderProduct(20), GOLD_PEACH.makeOrderProduct(10)))
							.shippingInformation(EUNJU_SHIPPING_INFORMATION.make())
							.orderState(OrderState.UNPAID)
							.orderDate(LocalDate.now()) // 오늘
							.build(),
					Order.builder()
							.orderNumber(OrderNumber.builder().orderNumber("4").build())
							.orderer(Orderer.builder().memberId("HealthMan").name("Jung Wooyoung").build())
							.orderLines(Arrays.asList(PEACH.makeOrderProduct(10), GOLD_PEACH.makeOrderProduct(10)))
							.shippingInformation(EUNJU_SHIPPING_INFORMATION.make())
							.orderState(OrderState.UNPAID)
							.orderDate(LocalDate.now()) // 오늘
							.build(),
					Order.builder()
							.orderNumber(OrderNumber.builder().orderNumber("5").build())
							.orderer(Orderer.builder().memberId("CookieMan").name("Kim Haksung").build())
							.orderLines(Arrays.asList(PEACH.makeOrderProduct(5), GOLD_PEACH.makeOrderProduct(5)))
							.shippingInformation(EUNJU_SHIPPING_INFORMATION.make())
							.orderState(OrderState.UNPAID) // 결제됨
							.orderDate(LocalDate.now()) // 오늘
							.build()
			);
			ordersForSave.forEach(orderRepository::save);
		}

		private void initProductDb() {

		}
	}
}
