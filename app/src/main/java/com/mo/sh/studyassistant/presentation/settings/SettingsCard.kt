package com.mo.sh.studyassistant.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    cornerRadius: Dp = 18.dp,
    hPadding: Dp = 12.dp,
    vPadding: Dp = 16.dp,
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = hPadding, vertical = vPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (iconRes != null) {
                    Icon(
                        painter = painterResource(iconRes),
                        title,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            content()
        }
    }
}

@Composable
fun SettingsLinkItem(
    modifier: Modifier = Modifier,
    title: String,
    url: String,
    cornerRadius: Dp = 18.dp,
    hPadding: Dp = 12.dp,
    vPadding: Dp = 16.dp,
    @DrawableRes iconRes: Int? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val context = LocalContext.current
    SettingsItem(
        modifier = modifier,
        title = title,
        cornerRadius = cornerRadius,
        hPadding = hPadding,
        vPadding = vPadding,
        iconRes = iconRes,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
        content = content
    )
}