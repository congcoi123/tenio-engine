open module tenio.engine {
  exports com.tenio.engine.heartbeat;
  exports com.tenio.engine.fsm.entity;
  exports com.tenio.engine.fsm;
  exports com.tenio.engine.message;
  exports com.tenio.engine.physic2d.graphic;
  exports com.tenio.engine.constant;
  exports com.tenio.engine.ecs.basis.implement;
  exports com.tenio.engine.ecs.basis;
  exports com.tenio.engine.heartbeat.ecs;
  exports com.tenio.engine.ecs.system;
  exports com.tenio.engine.ecs.system.implement;
  exports com.tenio.engine.physic2d.common;
  exports com.tenio.engine.physic2d.math;
  exports com.tenio.engine.physic2d.utility;
  exports com.tenio.engine.physic2d.graphic.window;
  requires tenio.common;
  requires jsr305;
  requires java.desktop;
  requires org.apache.logging.log4j;
}
