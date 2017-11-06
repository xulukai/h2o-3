from __future__ import print_function
import sys
sys.path.insert(1,"../../")
import h2o
import math
from tests import pyunit_utils
from h2o.utils.typechecks import assert_is_type
from h2o.frame import H2OFrame
import numpy as np

# global dictionaries storing model answers
g_iris_setosa_sepal_len=dict()
g_iris_versicolor_sepal_wid=dict()
g_iris_virginica_petal_wid=dict()
g_iris_versicolor_petal_len_NA_ignore=dict()
g_iris_versicolor_petal_len_NA_rm=dict()


def group_by_all():
    """
    I am testing the groupby median function and try to fix the error found
    in PUBDEV_4727.
    """

    # generate random dataset with factor column and real columns

    row_num = 1000
    col_num = 2 # 1 enum, 1 integer
    python_lists = np.random.randint(-5,5, (row_num, col_num))  # random lists
    # make H2O frame out of random list
    h2oframe = h2o.H2OFrame(python_obj=python_lists, column_types=["enum","int"], column_names=["factors", "numerics"])
    groupedMedian = h2oframe.group_by(["factors"]).median(na='rm')
    print(groupedMedian.get_frame()[0,0])

    # find median without NAs in frame
    h2oMedian = h2oframe.median(na_rm=True)
    assert_is_type(h2oMedian, list)
    numpmedian = list(np.median(python_lists, axis=0))  # medium calculated by numpy
    pyunit_utils.equal_two_arrays(numpmedian, h2oMedian, 1e-12, 1e-6)


if __name__ == "__main__":
    pyunit_utils.standalone_test(group_by_all)
else:
    group_by_all()
