package japgolly.microlibs.recursion

import scalaz.{-\/, Functor, Monad, \/, \/-}

final class AlgebraOps[F[_], A](private val self: Algebra[F, A]) extends AnyVal {

  def toAlgebraM[M[_]](implicit M: Monad[M]): AlgebraM[M, F, A] =
    fa => M.point(self(fa))

  def zip[B](that: Algebra[F, B])(implicit F: Functor[F]): Algebra[F, (A, B)] =
    fab => {
      val a = self(F.map(fab)(_._1))
      val b = that(F.map(fab)(_._2))
      (a, b)
    }

}

final class CoalgebraOps[F[_], A](private val self: Coalgebra[F, A]) extends AnyVal {

  def toCoalgebraM[M[_]](implicit M: Monad[M]): CoalgebraM[M, F, A] =
    a => M.point(self(a))

  def cozip[B](that: Coalgebra[F, B])(implicit F: Functor[F]): Coalgebra[F, A \/ B] = {
    case -\/(a) => F.map(self(a))(-\/(_))
    case \/-(b) => F.map(that(b))(\/-(_))
  }
}