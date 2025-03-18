package com.tenio.engine.physic2d.utility;

import com.tenio.common.utility.MathUtility;
import com.tenio.engine.physic2d.common.BaseGameEntity;
import com.tenio.engine.physic2d.math.Vector2;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Some useful entities functions.
 * <br>
 * <a
 * href=https://medium.com/swlh/understanding-3d-matrix-transforms-with-pixijs-c76da3f8bd8>3D
 * Matrix Transforms</a>
 */
public final class EntitiesRelationship {

  // Tests to see if an entity is overlapping any of a number of entities stored
  // in a list container
  public static <T extends BaseGameEntity, CT extends List<? extends T>> boolean isOverlapped(
      T ob, CT conOb) {
    return isOverlapped(ob, conOb, 40);
  }

  /**
   * Check if there is overlap between entities.
   *
   * @param ob                      the ob
   * @param conOb                   the conOb
   * @param minDistBetweenObstacles the min distance between obstacles
   * @param <T>                     the T class
   * @param <CT>                    the CT class
   * @return return <b>true</b> if there is overlap, <b>false</b> otherwise
   */
  public static <T extends BaseGameEntity, CT extends List<? extends T>> boolean isOverlapped(
      T ob, CT conOb,
      float minDistBetweenObstacles) {
    ListIterator<? extends T> it = conOb.listIterator();
    while (it.hasNext()) {
      T tmp = it.next();
      if (Geometry.isTwoCirclesOverlapped(ob.getPosition(),
          ob.getBoundingRadius() + minDistBetweenObstacles,
          tmp.getPosition(), tmp.getBoundingRadius())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Tags any entities contained in a list container that are within the radius of
   * the single entity parameter.
   *
   * @param entity              the entity
   * @param containerOfEntities the container of entities
   * @param radius              the radius
   * @param <T>                 the T class
   * @param <CT>                the CT class
   */
  public static <T extends BaseGameEntity, CT extends List<? extends T>> void tagNeighbors(
      final T entity, CT containerOfEntities, float radius) {
    // iterate through all entities checking for range
    ListIterator<? extends T> it = containerOfEntities.listIterator();

    while (it.hasNext()) {
      T curEntity = it.next();

      // first clear any current tag
      curEntity.enableTag(false);

      // the bounding radius of the other is taken into account by adding it
      // to the range
      Vector2 temp = Vector2.newInstance().set(entity.getPosition()).sub(curEntity.getPosition());

      // if entity within range, tag for further consideration. (working in
      // distance-squared space to avoid sqrts)
      if ((curEntity != entity) && (temp.getLengthSqr() < radius * radius)) {
        curEntity.enableTag(true);
      }
    }
  }

  /**
   * Given a pointer to an entity and a list container of pointers to nearby
   * entities, this function checks to see if there is an overlap between
   * entities. If there is, then the entities are moved away from each other.
   *
   * @param entity              the entity
   * @param containerOfEntities the container of entities
   * @param <T>                 the T class
   * @param <CT>                the CT class
   */
  public static <T extends BaseGameEntity,
      CT extends List<T>> void enforceNonPenetrationConstraint(
      final T entity, final CT containerOfEntities) {
    // iterate through all entities checking for any overlap of bounding radius
    ListIterator<T> it = containerOfEntities.listIterator();
    while (it.hasNext()) {
      T curEntity = it.next();
      // make sure we don't check against the individual
      if (curEntity == entity) {
        continue;
      }

      // calculate the distance between the positions of the entities
      Vector2 temp = Vector2.newInstance().set(entity.getPosition()).sub(curEntity.getPosition());

      float distFromEachOther = temp.getLength();
      
      // Skip if either entity has invalid radius
      if (Float.isNaN(entity.getBoundingRadius()) || Float.isNaN(curEntity.getBoundingRadius())) {
        continue;
      }

      // if this distance is smaller than the sum of their radius then this
      // entity must be moved away in the direction parallel to the
      // ToEntity vector
      float amountOfOverLap =
          curEntity.getBoundingRadius() + entity.getBoundingRadius() - distFromEachOther;

      if (amountOfOverLap >= 0 && distFromEachOther > 0) {
        // Create a normalized direction vector
        Vector2 direction = temp.normalize();
        // Calculate the new position
        Vector2 newPosition = Vector2.newInstance()
            .set(entity.getPosition())
            .add(direction.mul(amountOfOverLap));
        entity.setPosition(newPosition);
      }
    }
  }

  // Tests a line segment AB against a container of entities. First, a test
  // is made to confirm that the entity is within a specified range of the
  // one_to_ignore (positioned at vectorA). If within range the intersection test is
  // made.
  //
  // returns a list of all the entities that tested positive for intersection
  public static <T extends BaseGameEntity,
      CT extends List<T>> List<T> getEntityLineSegmentIntersections(
      final CT entities, String theOneToIgnore, Vector2 vectorA, Vector2 vectorB) {
    ListIterator<T> it = entities.listIterator();
    List<T> hits = new LinkedList<T>();

    // iterate through all entities checking against the line segment vectorA-vectorB
    while (it.hasNext()) {
      T curEntity = it.next();
      // if not within range or the entity being checked is the_one_to_ignore
      // just continue with the next entity
      if (curEntity.getId().equals(theOneToIgnore)) {
        continue;
      }

      // if the distance to AB is less than the entities bounding radius then
      // there is an intersection so add it to hits
      float distanceToSegment = Geometry.getDistancePointSegment(vectorA, vectorB, curEntity.getPosition());
      if (distanceToSegment < curEntity.getBoundingRadius()) {
        hits.add(curEntity);
      }
    }

    return hits;
  }

  // Tests a line segment AB against a container of entities. First, a test
  // is made to confirm that the entity is within a specified range of the
  // one_to_ignore (positioned at vectorA). If within range the intersection test is
  // made.
  // returns the closest entity that tested positive for intersection or NULL if
  // none found
  public static <T extends BaseGameEntity, CT extends
      List<T>> T getClosestEntityLineSegmentIntersection(
      final CT entities, String theOneToIgnore, Vector2 vectorA, Vector2 vectorB) {
    if (entities == null || entities.isEmpty()) {
      return null;
    }

    ListIterator<T> it = entities.listIterator();
    T closestEntity = null;
    float closestDist = MathUtility.MAX_FLOAT;

    while (it.hasNext()) {
      T curEntity = it.next();

      // Skip if entity is null or is the one to ignore
      if (curEntity == null || (theOneToIgnore != null && theOneToIgnore.equals(curEntity.getId()))) {
        continue;
      }

      // Skip if entity has invalid radius
      if (Float.isNaN(curEntity.getBoundingRadius()) || curEntity.getBoundingRadius() <= 0) {
        continue;
      }

      // Calculate distances
      float distanceToSegment = Geometry.getDistancePointSegment(vectorA, vectorB, curEntity.getPosition());
      if (Float.isNaN(distanceToSegment)) {
        continue;
      }

      float distanceToA = Vector2.newInstance().set(curEntity.getPosition()).getDistanceValue(vectorA);
      if (Float.isNaN(distanceToA)) {
        continue;
      }

      // Check for intersection
      if (distanceToSegment < curEntity.getBoundingRadius()) {
        if (distanceToA < closestDist) {
          closestDist = distanceToA;
          closestEntity = curEntity;
        }
      }
    }
    return closestEntity;
  }
}
