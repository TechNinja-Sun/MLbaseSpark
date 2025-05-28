import pandas as pd
import matplotlib.pyplot as plt
import os

# 指定输出目录
output_dir = 'output/clustering_result'

# 查找第一个 .csv 文件
csv_file = None
for filename in os.listdir(output_dir):
    if filename.endswith('.csv'):
        csv_file = os.path.join(output_dir, filename)
        break

if csv_file is None:
    raise FileNotFoundError("No CSV file found in output/clustering_result")

# 加载数据并绘图
df = pd.read_csv(csv_file)
plt.scatter(df['x'], df['y'], c=df['prediction'], cmap='viridis')
plt.xlabel('x')
plt.ylabel('y')
plt.title('KMeans Clustering Result')
plt.savefig('output/clustering_plot.png')
plt.show()
