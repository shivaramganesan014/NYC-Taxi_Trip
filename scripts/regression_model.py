import os
import sys
import glob
import torch
import torch.nn as nn
import torch.optim as optim
import pandas as pd
import matplotlib.pyplot as plt
from tqdm import trange

dir = sys.argv[1]
col1 = sys.argv[2]
col2 = sys.argv[3]

# determine the supported device
device = torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu")

PATH = f"/Users/bhavya/Desktop/spatial-project/{dir}"
EXT = "*.csv"
all_csv_files = [file for path, _, _ in os.walk(PATH) for file in glob.glob(os.path.join(path, EXT))]
print(len(all_csv_files))

# subset of data
head_csv_files = all_csv_files[:25]
# print(head_csv_files)

df = pd.concat((pd.read_csv(f) for f in head_csv_files), ignore_index=True)
df = df.loc[(df[col1] > 0) & (df[col1] < 2500), [col1, col2]]
print(df)

in1 = torch.from_numpy(df[col1].values).float().to(device)
in2 = torch.from_numpy(df[col2].values).float().to(device)
print(in1.shape, in2.shape)

combined = list(zip(in1, in2))

split_ratio = 0.8
split_index = int(len(combined) * split_ratio)

train_data = combined[:split_index]
test_data = combined[split_index:]

Xtr, ytr = zip(*train_data)
Xte, yte = zip(*test_data)

Xtrain = torch.stack(Xtr).float().to(device)
ytrain = torch.stack(ytr).float().to(device)
Xtest = torch.stack(Xte).float().to(device)
ytest = torch.stack(yte).float().to(device)
print(Xtrain.shape, ytrain.shape, Xtest.shape, ytest.shape)

# lr = 0.000000000005
# batch_size = 8192
# num_epochs = 1500

# distance amount relation
lr = 0.00001
batch_size = 2048
num_epochs = 500

class SimpleRegressionModel(nn.Module):
    def __init__(self):
        super(SimpleRegressionModel, self).__init__()
        self.linear = nn.Linear(1, 1)  # input size: 1, output size: 1

    def forward(self, x):
        return self.linear(x)

model = SimpleRegressionModel()
model = model.to(device)
criterion = nn.MSELoss()
optimizer = optim.SGD(model.parameters(), lr)

train_loss = []
test_loss = []

for epoch in (t := trange(num_epochs)):
    in1_loss = 0.0

    indices = torch.randperm(Xtrain.size(0))
    Xtr_shuffled = Xtrain[indices]
    ytr_shuffled = ytrain[indices]

    model.train()
    for i in range(0, Xtrain.size(0), batch_size):
        ins = Xtr_shuffled[i:i + batch_size].view(-1, 1)
        outs = ytr_shuffled[i:i + batch_size].view(-1, 1)

        outputs = model(ins)
        loss = criterion(outputs, outs)
        in1_loss += loss.item()

        # Backward pass and optimization
        optimizer.zero_grad()
        loss.backward()
        optimizer.step()

    # avg_loss = in1_loss / (Xtrain.size(0) / batch_size)
    train_loss.append((in1_loss/batch_size))
    t.set_description(f'epoch {epoch}; training loss: {(in1_loss/batch_size):.4f}')

    # test phase
    model.eval()
    with torch.no_grad():
        y_pred_test = model(Xtest.view(-1, 1))
        teloss = criterion(y_pred_test, ytest.view(-1, 1))
        test_loss.append(teloss.item())

# to make prediction from a given in1 amount
predictions = []
for x in Xtest:
    model.eval()
    with torch.no_grad():
        new_amount = torch.tensor([x], dtype=torch.float32).view(-1, 1)
        pred = model(new_amount)
        predictions.append(pred)

# plotting
plt.figure(figsize=(12, 4))
plt.plot(range(num_epochs), test_loss, label='Test Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()

plt.show()

print("\nMean Squared Error on Test Set:", criterion(torch.tensor(predictions), ytest).item())

# Visualize the results
plt.scatter(Xtest, ytest, label='True data')
plt.plot(Xtest, torch.tensor(predictions).numpy(), color='red', linewidth=3, label='Regression line')
plt.xlabel('Trip Distance')
plt.ylabel('Fare')
plt.legend()
plt.show()